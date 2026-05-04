package uk.ac.mmu.enterpriseprogrammingrest.Controllers.Request;
public class DeleteReq {

  private int id;

  public DeleteReq() {}

  public DeleteReq(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}