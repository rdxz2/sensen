package chandra.sensen;

public class Contract_Absensi {
    private String idabsen = "", tanggal_absen = "",  idumat= "", nama = "";

    public Contract_Absensi(String idabsen, String tanggal_absen, String idumat, String nama){
        this.idabsen = idabsen;
        this.tanggal_absen = tanggal_absen;
        this.idumat = idumat;
        this.nama = nama;
    }

    public String getIdAbsen(){return idabsen; }
    public void setIdAbsen(String idabsen){this.idabsen = idabsen; }

    public String getTanggalAbsen(){return tanggal_absen; }
    public void setTanggalAbsen(String tanggal_absen){this.tanggal_absen = tanggal_absen; }

    public String getIdUmat(){return idumat; }
    public void setIdUmat(String idumat){this.idumat = idumat; }

    public String getNama(){return nama; }
    public void setNama(String nama){this.nama = nama; }

}
