package systemapp.tbblessing.service;

import systemapp.tbblessing.model.*;
import java.util.List;

public interface TransaksiService {
    void addTransaksi(TransaksiModel transaksi);
    TransaksiModel updateTransaksi(Long idTransaksi, TransaksiModel transaksi);
    void deleteTransaksi(Long idTransaksi);
    TransaksiModel getTransaksiByIdTransaksi(Long idTransaksi);
    List<TransaksiModel> getAllTransaksi();
}
