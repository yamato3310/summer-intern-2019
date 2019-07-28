/*
 * This file is part of Nextbeat services.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package controllers.facility

import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, MessagesControllerComponents}
import persistence.facility.dao.FacilityDAO
import persistence.facility.model.Facility.formForFacilitySearch
import persistence.facility.model.Facility.formForFacilityEdit
import persistence.facility.model.Facility.formForFacilityAdd
import persistence.geo.model.Location
import persistence.geo.dao.LocationDAO
import model.site.facility.SiteViewValueFacilityList
import model.site.facility.SiteViewValueFacilityEdit
import model.site.facility.SiteViewValueFacilityAdd
import model.component.util.ViewValuePageLayout


// 施設
//~~~~~~~~~~~~~~~~~~~~~
class FacilityController @javax.inject.Inject()(
  val facilityDao: FacilityDAO,
  val daoLocation: LocationDAO,
  cc: MessagesControllerComponents
) extends AbstractController(cc) with I18nSupport {
  implicit lazy val executionContext = defaultExecutionContext

  /**
    * 施設一覧ページ
    */
  def list = Action.async { implicit request =>
    for {
      locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
      facilitySeq <- facilityDao.findAll
    } yield {
      val vv = SiteViewValueFacilityList(
        layout     = ViewValuePageLayout(id = request.uri),
        location   = locSeq,
        facilities = facilitySeq
      )
      Ok(views.html.site.facility.list.Main(vv, formForFacilitySearch))
    }
  }

  /**
   * 施設検索
   */
  def search = Action.async { implicit request =>
    formForFacilitySearch.bindFromRequest.fold(
      errors => {
       for {
          locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
          facilitySeq <- facilityDao.findAll
        } yield {
          val vv = SiteViewValueFacilityList(
            layout     = ViewValuePageLayout(id = request.uri),
            location   = locSeq,
            facilities = facilitySeq
          )
          BadRequest(views.html.site.facility.list.Main(vv, errors))
        }
      },
      form   => {
        for {
          locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
          facilitySeq <- form.locationIdOpt match {
            case Some(id) =>
              for {
                locations   <- daoLocation.filterByPrefId(id)
                facilitySeq <- facilityDao.filterByLocationIds(locations.map(_.id))
              } yield facilitySeq
            case None     => facilityDao.findAll
          }
        } yield {
          val vv = SiteViewValueFacilityList(
            layout     = ViewValuePageLayout(id = request.uri),
            location   = locSeq,
            facilities = facilitySeq
          )
          Ok(views.html.site.facility.list.Main(vv, formForFacilitySearch.fill(form)))
        }
      }
    )
  }

   /**
   * 施設編集画面
   */
  def show(id: Long) = Action.async { implicit request =>
    for {
        facility <- facilityDao.get(id)
      } yield {
        val header = SiteViewValueFacilityEdit(
          layout = ViewValuePageLayout(id = request.uri),
          facility = facility
        )
      Ok(views.html.site.facility.edit.Main(header, formForFacilityEdit))
    }
  }

  /**
   * 施設編集
   */
  def edit(id: Long) = Action.async { implicit request =>
    formForFacilityEdit.bindFromRequest.fold(
       errors => {
          for {
          locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
          facilitySeq <- facilityDao.findAll
        } yield {
          val vv = SiteViewValueFacilityList(
            layout     = ViewValuePageLayout(id = request.uri),
            location   = locSeq,
            facilities = facilitySeq
          )
          Ok(views.html.site.facility.list.Main(vv, formForFacilitySearch))
        }
      },
      form => {
        facilityDao.update(id, form.name, form.address, form.description)
        for {
          facility <- facilityDao.get(id)
        } yield {
          val header = SiteViewValueFacilityEdit(
          layout = ViewValuePageLayout(id = request.uri),
          facility = facility
        )
        Ok(views.html.site.facility.edit.Main(header, formForFacilityEdit))
        }
      }
    )
  }

  /**
   * 施設追加画面
   */
  def add() = Action.async { implicit request =>
    for {
      locSeq <- daoLocation.getCitys()
    } yield {
      val header = SiteViewValueFacilityAdd(
        layout = ViewValuePageLayout(id = request.uri),
        location = locSeq
      )
      Ok(views.html.site.facility.add.Main(header, formForFacilityAdd))
    }
  }

  /**
   * 施設追加
   */
   def create = TODO
  // def create = Action.async { implicit request =>
  //   formForFacilityEdit.bindFromRequest.fold(
  //     errors => {

  //     },
  //     form => {
        
  //     }
  //   )
  // }
}
