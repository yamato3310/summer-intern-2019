
package persistence.organization.model

import play.api.data._
import play.api.data.Forms._
import java.time.LocalDateTime
import persistence.geo.model.Location

case class Organization(
    id:          Option[Organization.Id],
    name:        String,                             // 施設名
    locationId:  Location.Id,                        // 地域ID
    address:     String,                             // 住所(詳細)
    updatedAt:   LocalDateTime = LocalDateTime.now,  // データ更新日
    createdAt:   LocalDateTime = LocalDateTime.now   // データ作成日
)

case class OrganizationAdd(
    name: String,
    locationId: Location.Id,
    address: String
)

case class OrganizationEdit(
    name: String,
    locationId: Location.Id,
    address: String
)

object Organization {
    type Id = Long

    def formForOrganizationAdd = Form(
        mapping(
            "name" -> text,
            "locationId" -> text,
            "address" -> text
        )(OrganizationAdd.apply)(OrganizationAdd.unapply)
    )

    def formForOrganizationEdit = Form(
        mapping(
            "name" -> text,
            "locationId" -> text,
            "address" -> text
        )(OrganizationEdit.apply)(OrganizationEdit.unapply)
    )
}
