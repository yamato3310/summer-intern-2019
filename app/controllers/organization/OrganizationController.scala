package controllers.organization

import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, MessagesControllerComponents}

import persistence.organization.dao.OrganizationDao
import model.site.facility.SiteViewValueOrganizationList

import persistence.facility.dao.FacilityDAO
import persistence.facility.model.Facility.formForFacilitySearch
import persistence.facility.model.Facility.formForFacilityEdit
import persistence.facility.model.Facility.formForFacilityAdd

import persistence.geo.model.Location
import persistence.geo.dao.LocationDAO


class Organization @javax.inject.Inject()(
    val organization: OrganizationDAO,
    val daoLocation: LocationDAO,
    val facilityDao: FacilityDAO,
    cc: MessagesControllerComponents
) extends AbstractController(cc) with I18nSupport {
    implicit lazy val executionContext = defaultExecutionContext

    def list() = Action { implicit request =>
        for {
            organizationSeq <- organizationDAO.findall
        } yield {
            val vv = SiteViewValueOrganizationList(
                layout        = ViewValuePageLayout(id = request.uri),
                organizations = organizationSeq
            )
            Ok(views.html.site.organization.list.Main(vv)
        }
    }
    
}
