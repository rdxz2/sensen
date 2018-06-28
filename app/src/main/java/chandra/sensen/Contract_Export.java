package chandra.sensen;

public class Contract_Export {
    private String idabsen = "", idumat = "", nama = "";

    public Contract_Export(String idabsen, String idumat, String nama){
        this.idabsen = idabsen;
        this.idumat = idumat;
        this.nama = nama;
    }

    public String getIdabsen(){return idabsen; }
    public void setIdabsen(String idabsen){this.idabsen = idabsen; }

    public String getIdUmat(){return idumat; }
    public void setIdUmat(String nama){this.idumat = idumat; }

    public String getNama(){return nama; }
    public void setNama(String nama){this.nama = nama; }
}
