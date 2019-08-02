package controllers.organization

import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, MessagesControllerComponents}

import persistence.organization.dao.OrganizationDAO
import model.site.organization.SiteViewValueOrganizationList
import model.site.organization.SiteViewValueOrganizationAdd
import model.site.organization.SiteViewValueOrganizationShow
import model.site.organization.SiteViewValueOrganizationEdit
import persistence.organization.model.Organization.formForOrganizationAdd
import persistence.organization.model.Organization.formForOrganizationEdit

import persistence.facility.dao.FacilityDAO
import persistence.facility.model.Facility.formForFacilitySearch
import persistence.facility.model.Facility.formForFacilityEdit
import persistence.facility.model.Facility.formForFacilityAdd

import persistence.geo.model.Location
import persistence.geo.dao.LocationDAO
import model.component.util.ViewValuePageLayout


class OrganizationController @javax.inject.Inject()(
    val organizationDao: OrganizationDAO,
    val daoLocation: LocationDAO,
    val facilityDao: FacilityDAO,
    cc: MessagesControllerComponents
) extends AbstractController(cc) with I18nSupport {
    implicit lazy val executionContext = defaultExecutionContext

    def list() = Action.async { implicit request =>
        for {
            organizationSeq <- organizationDao.findAll
        } yield {
            val vv = SiteViewValueOrganizationList(
                layout        = ViewValuePageLayout(id = request.uri),
                organizations = organizationSeq
            )
            Ok(views.html.site.organization.list.Main(vv))
        }
    }

    def add() = Action.async { implicit request =>
        for {
            locSeq <- daoLocation.getCitys()
        } yield {
            val vv = SiteViewValueOrganizationAdd(
                location      = locSeq,
                layout        = ViewValuePageLayout(id = request.uri),
            )
            Ok(views.html.site.organization.add.Main(vv, formForOrganizationAdd))
        }
    }

    def show(id: Long) = Action.async { implicit request =>
         for {
            organization <- organizationDao.get(id)
        } yield {
            val vv = SiteViewValueOrganizationShow(
                layout        = ViewValuePageLayout(id = request.uri),
                organization = organization
            )
            Ok(views.html.site.organization.show.Main(vv))
        }
    }

    def edit(id: Long) = Action.async { implicit request =>
         for {
            organization <- organizationDao.get(id)
            locSeq <- daoLocation.getCitys()
        } yield {
            val vv = SiteViewValueOrganizationEdit(
                layout        = ViewValuePageLayout(id = request.uri),
                organization  = organization,
                location      = locSeq

            )
            Ok(views.html.site.organization.edit.Main(vv, formForOrganizationEdit))
        }
    }

    def update(id: Long) = Action.async { implicit request =>
         formForOrganizationAdd.bindFromRequest.fold(
            error => {
                for {
                    organizationSeq <- organizationDao.findAll
                } yield {
                    val vv = SiteViewValueOrganizationList(
                        layout        = ViewValuePageLayout(id = request.uri),
                        organizations = organizationSeq
                    )
                    Ok(views.html.site.organization.list.Main(vv))
                }
            },
            form => {
                organizationDao.update(id, form.locationId, form.name, form.address)
                for {
                    organizationSeq <- organizationDao.findAll
                } yield {
                    val vv = SiteViewValueOrganizationList(
                        layout        = ViewValuePageLayout(id = request.uri),
                        organizations = organizationSeq
                    )
                    Ok(views.html.site.organization.list.Main(vv))
                }
            }
        )
    }


    def create() = Action.async { implicit request => 
        formForOrganizationAdd.bindFromRequest.fold(
            error => {
                for {
                    organizationSeq <- organizationDao.findAll
                } yield {
                    val vv = SiteViewValueOrganizationList(
                        layout        = ViewValuePageLayout(id = request.uri),
                        organizations = organizationSeq
                    )
                    Ok(views.html.site.organization.list.Main(vv))
                }
            },
            form => {
                organizationDao.create(form.locationId, form.name, form.address)
                for {
                    organizationSeq <- organizationDao.findAll
                } yield {
                    val vv = SiteViewValueOrganizationList(
                        layout        = ViewValuePageLayout(id = request.uri),
                        organizations = organizationSeq
                    )
                    Ok(views.html.site.organization.list.Main(vv))
                }
            }
        )
    }

    def delete(id: Long) = Action.async { implicit request =>
        organizationDao.delete(id)
        for {
            organizationSeq <- organizationDao.findAll
        } yield {
            val vv = SiteViewValueOrganizationList(
                layout        = ViewValuePageLayout(id = request.uri),
                organizations = organizationSeq
            )
            Ok(views.html.site.organization.list.Main(vv))
        }
    }
}
