
package persistence.organization.dao

import java.time.LocalDateTime
import scala.concurrent.Future

import slick.jdbc.JdbcProfile
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import persistence.organization.model.Organization
import persistence.geo.model.Location

class OrganizationDAO @javax.inject.Inject()(
    val dbConfigProvider: DatabaseConfigProvider
) extends HasDatabaseConfigProvider[JdbcProfile] {
    import profile.api._

    lazy val slick = TableQuery[OrganizationTable]

    def findAll: Future[Seq[Organization]] =
        db.run {
          slick.result
        }
        
    class OrganizationTable() extends Table[Organization](tag, "organization") {
        def id            = column[Organization.Id] ("id", O.PrimaryKey, O.AutoInc)
        def locationId    = column[Location.Id] ("location_id")
        def name          = column[String] ("name")
        def address       = column[String] ("address")
        def updatedAt     = column[LocalDateTime]  ("updated_at")
        def createdAt     = column[LocalDateTime]  ("created_at")

        def * = (
          id.?, locationId, name, address, updatedAt, createdAt
        ) <> (
          /** The bidirectional mappings : Tuple(table) => Model */
          (Organization.apply _).tupled,
          /** The bidirectional mappings : Model => Tuple(table) */
          (v: TableElementType) => Organization.unapply(v).map(_.copy(
            _6 = LocalDateTime.now
          ))
        )
    }
}

