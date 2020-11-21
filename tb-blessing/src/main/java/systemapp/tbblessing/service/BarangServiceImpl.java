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
public class BarangServiceImpl implements BarangService {
    @Autowired
    BarangDb barangDb;

    @Override
    public void addBarang(BarangModel barangModel){
        barangDb.save(barangModel);
    }

    @Override
    public BarangModel updateBarang(Long idBarang, BarangModel barangModel){
        BarangModel barang = getBarangByIdBarang(idBarang);
        barang.setHargaBeliBarang(barangModel.getHargaBeliBarang());
        barang.setNamaBarang(barangModel.getNamaBarang());
        barang.setStockBarang(barangModel.getStockBarang());
        return barangDb.save(barang);
    }

    @Override
    public BarangModel getBarangByIdBarang(Long idBarang){
        Optional<BarangModel> barang = barangDb.findByIdBarang(idBarang);
        if(barang.isPresent()) {
            return barang.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public BarangModel getBarangByNamaBarang(String namaBarang){
        Optional<BarangModel> barang = barangDb.findByNamaBarang(namaBarang);
        if(barang.isPresent()) {
            return barang.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<BarangModel> getAllBarang() {
        return barangDb.findAll();
    }

    @Override
    public List<BarangModel> getByStockBarangLessThanEqual(int input) {
        return barangDb.findByStockBarangLessThanEqual(input);
    }

    @Override
    public void deleteBarang(Long idBarang) {
        BarangModel barang = getBarangByIdBarang(idBarang);
        barangDb.delete(barang);
    }
}