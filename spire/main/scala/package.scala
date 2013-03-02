package scalaz.contrib

package object spire
  extends GroupOps
  with MulGroupOps
  with AddGroupOps
  with RingOps {

  object additiveGroups extends AddGroupOps
  object groups extends GroupOps
  object multiplicativeGroups extends MulGroupOps
  object rings extends RingOps

}

// vim: expandtab:ts=2:sw=2
