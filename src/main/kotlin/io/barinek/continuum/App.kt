package io.barinek.continuum

import io.barinek.continuum.accounts.AccountController
import io.barinek.continuum.accounts.AccountDataGateway
import io.barinek.continuum.accounts.RegistrationController
import io.barinek.continuum.accounts.RegistrationService
import io.barinek.continuum.allocations.AllocationController
import io.barinek.continuum.allocations.AllocationDataGateway
import io.barinek.continuum.backlog.StoryController
import io.barinek.continuum.backlog.StoryDataGateway
import io.barinek.continuum.projects.ProjectController
import io.barinek.continuum.projects.ProjectDataGateway
import io.barinek.continuum.timesheets.TimeEntryController
import io.barinek.continuum.timesheets.TimeEntryDataGateway
import io.barinek.continuum.users.UserController
import io.barinek.continuum.users.UserDataGateway
import io.barinek.continuum.restsupport.BasicApp
import io.barinek.continuum.jdbcsupport.JdbcTemplate
import io.barinek.continuum.jdbcsupport.TransactionManager
import org.eclipse.jetty.server.handler.HandlerList
import java.util.*

class App : BasicApp() {
    override fun getPort() = System.getenv("PORT").toInt()

    override fun handlerList(transactionManager: TransactionManager, template: JdbcTemplate): HandlerList {
        val userDataGateway = UserDataGateway(template)
        val accountDataGateway = AccountDataGateway(template)

        return HandlerList().apply { // ordered

            addHandler(RegistrationController(mapper, RegistrationService(transactionManager, userDataGateway, accountDataGateway)))
            addHandler(AccountController(mapper, accountDataGateway))
            addHandler(AllocationController(mapper, AllocationDataGateway(template)))
            addHandler(UserController(mapper, userDataGateway))
            addHandler(ProjectController(mapper, ProjectDataGateway(template)))
            addHandler(StoryController(mapper, StoryDataGateway(template)))
            addHandler(TimeEntryController(mapper, TimeEntryDataGateway(template)))
        }
    }
}

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    App().start()
}