package scalaz.contrib

package object spire
  extends GroupOps
  with EqualityOps
  with MulGroupOps
  with AddGroupOps
  with RingOps {

  object equality extends EqualityOps
  object additiveGroups extends AddGroupOps
  object groups extends GroupOps
  object multiplicativeGroups extends MulGroupOps
  object rings extends RingOps

}

// vim: expandtab:ts=2:sw=2
