package chandra.sensen;

public class Contract_Umat {
    private String idumat = "", nama = "", tgl_lahir = "", alamat = "", foto = "";

    public Contract_Umat(String idumat, String nama, String tgl_lahir, String alamat){
        this.idumat = idumat;
        this.nama = nama;
        this.tgl_lahir = tgl_lahir;
        this.alamat = alamat;
    }

    public String getIdUmat(){return idumat; }
    public void setIdUmat(String nama){this.idumat = idumat; }

    public String getNama(){return nama; }
    public void setNama(String nama){this.nama = nama; }

    public String getTglLahir(){return tgl_lahir; }
    public void setTglLahir(String tgl_lahir){this.tgl_lahir = tgl_lahir; }

    public String getAlamat(){return alamat; }
    public void setAlamat(String alamat){this.alamat = alamat; }

    public String getFoto(){return foto;}
    public void setFoto(String foto){this.foto = foto; }

}
