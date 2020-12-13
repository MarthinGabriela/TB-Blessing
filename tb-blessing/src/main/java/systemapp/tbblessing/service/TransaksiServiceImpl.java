package systemapp.tbblessing.service;

import systemapp.tbblessing.model.*;
import systemapp.tbblessing.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class TransaksiServiceImpl implements TransaksiService {
    @Autowired
    TransaksiDb transaksiDb;

    @Override
    public void addTransaksi(TransaksiModel transaksi) {
        transaksiDb.save(transaksi);
    }

    @Override
    public TransaksiModel updateTransaksi(Long idTransaksi, TransaksiModel transaksi) {
        return transaksiDb.save(transaksi);
    }

    @Override
    public void deleteTransaksi(Long idTransaksi) {
        TransaksiModel transaksi = getTransaksiByIdTransaksi(idTransaksi);
        transaksiDb.delete(transaksi);
    }

    @Override
    public TransaksiModel getTransaksiByIdTransaksi(Long idTransaksi) {
        Optional<TransaksiModel> transaksi = transaksiDb.findByIdTransaksi(idTransaksi);
        if(transaksi.isPresent()) {
            return transaksi.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<TransaksiModel> getTransaksiByNamaPembeli(String namaPembeli) {
        return transaksiDb.findByNamaPembeli(namaPembeli);
    }

    @Override
    public List<TransaksiModel> getAllTransaksi() {
        return transaksiDb.findAll();
    }

    @Override
    public void updateNominalTransaksi(TransaksiModel transaksi) {
        Long nominal = 0L;

        for(int i = 0; i < transaksi.getListBarangJual().size(); i++) {
            nominal += transaksi.getListBarangJual().get(i).getHargaJual();
        }

        Long price = (long) 100 - transaksi.getDiskon();
        Long drnominal = (long) (nominal * price)/100;
        nominal = drnominal;

        for(int i = 0; i < transaksi.getListBarangRetur().size(); i++) {
            nominal -= transaksi.getListBarangRetur().get(i).getHargaRetur();
        }

        transaksi.setNominalTransaksi(nominal);
        transaksiDb.save(transaksi);
    }
}
