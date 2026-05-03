package uk.ac.mmu.enterpriseprogrammingrest.Controllers.Response;

import java.util.List;
import uk.ac.mmu.enterpriseprogrammingrest.model.data.BookVO;

public record GetRes(
    List<BookVO> books,
    int page,
    int totalPages
) {

}
