
package persistence.organization.model

import play.api.data._
import play.api.data.Forms._
import java.time.LocalDateTime

case class Organization(
    id:          Option[Organization.Id],
    name:        String,                             // 施設名
    locationId:  Location.Id,                        // 地域ID
    address:     String,                             // 住所(詳細)
    updatedAt:   LocalDateTime = LocalDateTime.now,  // データ更新日
    createdAt:   LocalDateTime = LocalDateTime.now   // データ作成日
)

object Organization {
    type Id = Long
}
