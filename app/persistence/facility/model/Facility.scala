/*
 * This file is part of the Nextbeat services.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package persistence.facility.model

import play.api.data._
import play.api.data.Forms._
import java.time.LocalDateTime
import persistence.geo.model.Location

// 施設情報 (sample)
//~~~~~~~~~~~~~
case class Facility(
  id:          Option[Facility.Id],                // 施設ID
  locationId:  Location.Id,                        // 地域ID
  name:        String,                             // 施設名
  address:     String,                             // 住所(詳細)
  description: String,                             // 施設説明
  updatedAt:   LocalDateTime = LocalDateTime.now,  // データ更新日
  createdAt:   LocalDateTime = LocalDateTime.now   // データ作成日
)

// 施設検索
case class FacilitySearch(
  locationIdOpt: Option[Location.Id]
)

// 施設編集
case class FacilityEdit(
  name: String,
  address: String,
  description: String,
)

// 施設追加
case class FacilityAdd(
  locationIdOpt: Option[Location.Id]
  name: String,
  address: String,
  description: String,
)

// コンパニオンオブジェクト
//~~~~~~~~~~~~~~~~~~~~~~~~~~
object Facility {

  // --[ 管理ID ]---------------------------------------------------------------
  type Id = Long

  // --[ フォーム定義 ]---------------------------------------------------------
  val formForFacilitySearch = Form(
    mapping(
      "locationId" -> optional(text),
    )(FacilitySearch.apply)(FacilitySearch.unapply)
  )

  // --[ 編集用のフォームを定義 ]---------------------------------------------------------
  val formForFacilityEdit = Form (
    mapping(
      "name" -> text,
      "address" -> text,
      "description" -> text
    )(FacilityEdit.apply)(FacilityEdit.unapply)
  )

  val formForFacilityAdd = Form (
    mapping(
      "locationId" -> optional(text),
      "name" -> text,
      "address" -> text,
      "description" -> text
    )(FacilityCreate.apply)(FacilityCreate.unapply)
  )
}

