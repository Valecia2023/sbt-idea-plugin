package org.jetbrains.sbtidea.packaging

import org.jetbrains.sbtidea.packaging.structure.{PackagedProjectNode, PackagingMethod as StructurePackagingMethod}
import org.jetbrains.sbtidea.structure.ProjectNode

package object mappings {

  implicit class ProjectNodeExt(val node: PackagedProjectNode) extends AnyVal {
    def mmd: MappingMetaData =
      MappingMetaData(shading = node.packagingOptions.shadePatterns,
        excludeFilter = node.packagingOptions.excludeFilter,
        static = true,
        project = Some(node.name),
        kind = MAPPING_KIND.UNDEFINED)

    private def collectNodes(node: PackagedProjectNode)(predicate: PackagedProjectNode => Boolean): Seq[ProjectNode] = {
      val lst = node.parents.filter(predicate)
      lst ++ node.parents.flatMap(collectNodes(_)(predicate))
    }

    def collectStandaloneParents: Seq[ProjectNode] = collectNodes(node) {
      _.packagingOptions.packageMethod.isInstanceOf[StructurePackagingMethod.Standalone]
    }

    /**
      * @return true if node has any parent nodes with classes
      */
    def hasRealParents: Boolean = collectNodes(node) { n =>
      n.packagingOptions.packageMethod match {
        case _: StructurePackagingMethod.Skip | _:StructurePackagingMethod.DepsOnly => false
        case _ => true
      }
    }.nonEmpty
  }

}
