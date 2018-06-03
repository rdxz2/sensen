package chandra.sensen;

public class Contract_Umat {
    private String nama, id;

    public Contract_Umat(String nama, String id){
        this.nama = nama;
        this.id = id;
    }

    public String getNama(){return nama; }
    public void setNama(String nama){this.nama = nama; }
    public String getId(){return id; }
    public void setId(String nama){this.id = id; }

}
