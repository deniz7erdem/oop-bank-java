/*
    Deniz Erdem 20360859101
 */
package dnybank;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.event.*;

public class Dnybank {

    public static void main(String[] args) {

        Musteri a= new Musteri("ayd","er","ayder123",123456789);
        Musteri b= new Musteri("bura","bas","burabas456",456678941);
        System.out.println(a.getAd());
        System.out.println(b.getAd());
        a.hesapEkle(1200,"v");
        a.hesapEkle(500, "y");
        b.hesapEkle(300, "v");
        b.hesapEkle(0, "y");
        System.out.println(a.getHesaplar(0).getBakiye());
        System.out.println(b.getHesaplar(0).getBakiye());
        System.out.println(a.getHesaplar(0).getIban());
        
        for(VadesizHesap i:Musteri.vadesizDB){
            if(i.getIban()==a.getHesaplar(0).getIban()){
                i.paraTransferi(100, b.getHesaplar(0).getIban());
            }
                
        }
        System.out.println(a.getHesaplar(0).getBakiye());
        System.out.println(b.getHesaplar(0).getBakiye());
        gui penc = new gui("DNYBANK");
    }

}

/*-----------------------------------------------------------------------------------*/
 /*-----------------------------------------------------------------------------------*/
class Kisi {

    //Gerekli değişkenler ve methodlar
    private String ad;
    private String soyad;
    private String email;
    private long telefonNumarasi;

    public Kisi(String ad, String soyad, String email, long telefonNumarasi) {
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.telefonNumarasi = telefonNumarasi;
    }

    public String getAd() {
        return this.ad;
    }

    public void setAd(String yAd) {
        this.ad = yAd;
    }

    public String getSoy() {
        return this.soyad;
    }

    public String getMail() {
        return this.email;
    }

    public long getTelefon() {
        return this.telefonNumarasi;
    }

    @Override
    public String toString() {
        return "Ad:" + ad + " Soyad:" + soyad;
    }

}

/*-----------------------------------------------------------------------------------*/
 /*-----------------------------------------------------------------------------------*/
class BankaPersoneli extends Kisi {

    //"Random" sınıfı ile rasgele bir integer üreterek "personelID"'ye atıyorum
    Random r = new Random();
    private int personelID = r.nextInt()& Integer.MAX_VALUE;;
    private ArrayList<Musteri> musteriler = new ArrayList<Musteri>();

    public BankaPersoneli(String ad, String soyad, String email, long telefonNumarasi) {
        super(ad, soyad, email, telefonNumarasi);
    }

    public void musteriEkle(Musteri musteri) {
        //methoda verilen "Musteri" sınıfından nesneyi "musteriler" listesine ekliyorum
        this.musteriler.add(musteri);
    }

    @Override
    public String toString() {
        return "Personel id:" + personelID;
    }
}

/*-----------------------------------------------------------------------------------*/
 /*-----------------------------------------------------------------------------------*/
class Musteri extends Kisi {

    Random r = new Random();
    private int musteriNumarasi = r.nextInt()& Integer.MAX_VALUE;;
    private ArrayList<BankaHesabi> hesaplar = new ArrayList<BankaHesabi>();
    private ArrayList<KrediKarti> krediKartlari = new ArrayList<KrediKarti>();
    //Transfer işelemlerinde tüm vadesiz hesapları aramak için ortak bir liste oluşturuyorum
    public static ArrayList<VadesizHesap> vadesizDB = new ArrayList<VadesizHesap>();
    //Kredi kartı borç ödeme işlemlerinde tüm kartları aramak için ortak bir liste oluşturuyorum
    public static ArrayList<KrediKarti> krediDB = new ArrayList<KrediKarti>();

    public Musteri(String ad, String soyad, String email, long telefonNumarasi) {
        super(ad, soyad, email, telefonNumarasi);
    }

    public void hesapEkle(double bakiye, String tur) {
        /*methoda verilen "tur" değikeni ile yatırım mı yoksa vadesiz hesap mı açılacağını 
          kontrol ediyorum, eşleşme olduğunda nesneyi oluşturup gerekli listelere ekliyorum */
        if (tur.equals("v")) {
            VadesizHesap bnk = new VadesizHesap(bakiye);
            this.hesaplar.add(bnk);
            Musteri.vadesizDB.add(bnk);
        } else if (tur.equals("y")) {
            YatirimHesabi ytr = new YatirimHesabi(bakiye);
            this.hesaplar.add(ytr);
        } else {
            //yanlış tur değişkeni ile karşılaşıldığında hata gözükmesini sağlıyorum
            System.out.println("hesapEkle hata: hesap türü seç");
        }

    }

    public void krediKartiEkle(double limit) {
        //methoda gelen limit değerine sahip KrediKarti nesnesi oluşturup listelere ekliyorum
        KrediKarti krt = new KrediKarti(limit, 0);
        this.krediKartlari.add(krt);
        Musteri.krediDB.add(krt);
    }

    public void hesapSil(int iban) {
        /* aranan ibanın bakiyesini kontrol ediyorum. Sıfırdan farklı ise hata veriyor. 
        Sıfıra eşit ise silme işlemlerini yapıyor */
        for (int i = 0; i < hesaplar.size(); i++) {
            if (hesaplar.get(i).equals(iban)) {
                if (hesaplar.get(i).equals(0)) {
                    hesaplar.remove(i);
                } else {
                    System.out.println("lütfen öncelikle bakiyenizi başka bir hesaba aktarınız");
                }

            }
        }
    }

    public void krediKartiSil(int kartNumarasi) {
        /* aranan kart numarasından borcunu kontrol ediyorum. Sıfırdan farklı ise hata veriyor. 
        Sıfıra eşit ise silme işlemlerini yapıyor */
        for (int i = 0; i < krediKartlari.size(); i++) {
            if (krediKartlari.get(i).equals(kartNumarasi)) {
                if (krediKartlari.get(i).equals(0.0)) {
                    krediKartlari.remove(i);
                } else {
                    System.out.println("lütfen öncelikle borç ödemesi yapınız");
                }

            }
        }
    }

    public ArrayList getIban() {
        return hesaplar;
    }

    public ArrayList getKK() {
        return krediKartlari;
    }
    public BankaHesabi getHesaplar(int i){
        return hesaplar.get(i);
    }
    @Override
    public String toString() {
        return "Musteri no:" + musteriNumarasi;
    }
}

/*-----------------------------------------------------------------------------------*/
 /*-----------------------------------------------------------------------------------*/
class KrediKarti {

    Random r = new Random();
    private int kartNumarasi = r.nextInt()& Integer.MAX_VALUE;;
    private double limit;
    private double guncelBorc;
    private double kullanilabilirLimit;

    public KrediKarti(double limit, double guncelBorc) {
        //kullanılabilirLimiti burada hesaplatarak atamaları yapıyorum.
        this.limit = limit;
        this.guncelBorc = guncelBorc;
        this.kullanilabilirLimit = limit - guncelBorc;
    }

    public double getKartNumarasi() {
        return this.kartNumarasi;
    }

    public double getLimit() {
        return this.limit;
    }

    public double getGuncelBorc() {
        return this.guncelBorc;
    }

    public double getKullanilabilirLimit() {
        return this.kullanilabilirLimit;
    }

    public void setLimit(double yeniLimit) {
        this.limit = yeniLimit;
        //her işlemde kullanılabilir limiti elle girmemek için burada hesaplatıyorum.
        this.kullanilabilirLimit = limit - guncelBorc;
    }

    public void setGuncelBorc(double yeniGuncelBorc) {
        this.guncelBorc = yeniGuncelBorc;
        this.kullanilabilirLimit = limit - guncelBorc;
    }

    @Override
    public String toString() {
        return "Kart Numarası:" + kartNumarasi;
    }
}

/*-----------------------------------------------------------------------------------*/
 /*-----------------------------------------------------------------------------------*/
class BankaHesabi {

    Random r = new Random();
    private int iban = r.nextInt()& Integer.MAX_VALUE;;
    private double bakiye;

    public BankaHesabi(double bakiye) {
        this.bakiye = bakiye;
    }

    public double getBakiye() {
        return this.bakiye;
    }

    public void setBakiye(double yeniBakiye) {
        this.bakiye = yeniBakiye;
    }

    public int getIban() {
        return this.iban;
    }

    @Override
    public String toString() {
        return "iban:" + iban;
    }

}

/*-----------------------------------------------------------------------------------*/
 /*-----------------------------------------------------------------------------------*/
class VadesizHesap extends BankaHesabi {

    private final String hesapTuru = "Vadesiz";

    public VadesizHesap(double bakiye) {
        super(bakiye);
    }

    public void paraTransferi(double tutar, int yatirilan) {
        /*methoda transfer tutarı ve yatırılacak hesabın ibanı veriliyor. 
        Yatırılacak tutarın hesapta varolduğundan emin olduktan sonra yatırılacak hesap aranıyor.
        Ardından işlemler yapılarak hesapların son bakiyesi set metodları ile nesnelere kayıt ediliyor*/
        if (super.getBakiye() >= tutar) {
            for (VadesizHesap i : Musteri.vadesizDB) {
                if (i.getIban() == yatirilan) {
                    double sonCekilen = super.getBakiye() - tutar;
                    double sonYatirilan = i.getBakiye() + tutar;
                    super.setBakiye(sonCekilen);
                    i.setBakiye(sonYatirilan);
                }
            }
        } else {
            //hesapta istenen aktarım tutarı kadar bakiye yoksa hata veriyor
            System.out.println("Bakiye yetersiz.");
        }

    }

    public void krediKartiBorcOdeme(int kartNumarasi, double tutar) {
        /*methoda kart numarası ve ödenmek istenen tutar veriliyor.
        Ödenmek istenen tutarın hesapta var olduğundan emin olduktan sonra kredi kartı aranıyor
        Akabinde işlemler yapılarak kartın son limiti ve hesabın son bakiyesi metodlar ile nesnelere kayıt ediliyor*/
        if (super.getBakiye() >= tutar) {
            for (KrediKarti i : Musteri.krediDB) {
                if (i.getKartNumarasi() == kartNumarasi) {
                    double sonHesap = super.getBakiye() - tutar;
                    double sonKart = i.getGuncelBorc() - tutar;
                    super.setBakiye(sonHesap);
                    i.setGuncelBorc(sonKart);
                }
            }
        } else {
            System.out.println("Bakiye yetersiz.");
        }

    }

    @Override
    public String toString() {
        return hesapTuru + "hesap iban:" + super.getIban();
    }
}

/*-----------------------------------------------------------------------------------*/
 /*-----------------------------------------------------------------------------------*/
class YatirimHesabi extends BankaHesabi {

    private final String hesapTuru = "Yatırım";

    public YatirimHesabi(double bakiye) {
        super(bakiye);
    }

    public void paraEkle(int cekilen, double tutar) {
        /*methoda paranın çekileceği hesap ve çekilecek tutar veriliyor
        istenen hesap bulunduktan sonra içerisinde çekilecek tutar var olup olmadığı kontrol ediliyor.
        Tutar var ise aktarım işlemleri metodlar ile nesnelere kaydediliyor*/
        for (VadesizHesap i : Musteri.vadesizDB) {
            if (i.getIban() == cekilen) {
                if (i.getBakiye() >= tutar) {
                    super.setBakiye(super.getBakiye() + tutar);
                    i.setBakiye(i.getBakiye() - tutar);
                } else {
                    System.out.println("Bakiye yetersiz.");
                }
            }
        }
    }

    public void paraCek(int yatirilacak, double tutar) {
        /*paraEkle metodu ile benzer mantık yatırılacak tutar ve hesap giriliyor
        hesap bulunduktan sonra istenen tutarın yatırım hesabında var olup olmadığına bakılıyor.
        Tutar var ise aktarımö işlemleri metodlar ile nesnelere kaydediliyor*/
        for (VadesizHesap i : Musteri.vadesizDB) {
            if (i.getIban() == yatirilacak) {
                if (super.getBakiye() >= tutar) {
                    i.setBakiye(i.getBakiye() + tutar);
                    super.setBakiye(super.getBakiye() - tutar);
                } else {
                    System.out.println("Bakiye yetersiz.");
                }
            }
        }
    }

    public String hspTur() {
        return this.hesapTuru;
    }

    @Override
    public String toString() {

        return hesapTuru + "hesabı iban:" + super.getIban();
    }
}

/*-----------------------------------------------------------------------------------*/
 /*-----------------------------------------------------------------------------------*/
class gui extends JFrame implements ActionListener {

    JButton musteriEk, musteriBil, hesapBil, krediBil;
    JLabel lblAd, lblSoy, lblMail, lblTel, deniz, kntrlAd, kntrlSoy, kntrlMail, kntrlTel;
    JTextField txtAd, txtSoy, txtMail, txtTel;

    gui(String baslik) {
        super(baslik);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        //TextFieldların hangi amaca hizmet ettiğini gösteren Labellar
        lblAd = new JLabel("Adı:");
        lblAd.setBounds(40, 30, 50, 50);
        this.add(lblAd);
        lblSoy = new JLabel("Soyadı:");
        lblSoy.setBounds(40, 80, 50, 50);
        this.add(lblSoy);
        lblMail = new JLabel("Mail:");
        lblMail.setBounds(40, 130, 50, 50);
        this.add(lblMail);
        lblTel = new JLabel("Telefon:");
        lblTel.setBounds(40, 190, 50, 50);
        this.add(lblTel);

        //sağ altta numaram yazan label
        deniz = new JLabel("Deniz Erdem 20360859101");
        deniz.setBounds(500, 500, 200, 100);
        this.add(deniz);

        //Müşteri bilgilerinin girileceği alanlar
        txtAd = new JTextField();
        txtAd.setBounds(100, 40, 150, 25);
        this.add(txtAd);
        txtSoy = new JTextField();
        txtSoy.setBounds(100, 90, 150, 25);
        this.add(txtSoy);
        txtMail = new JTextField();
        txtMail.setBounds(100, 140, 150, 25);
        this.add(txtMail);
        txtTel = new JTextField();
        txtTel.setBounds(100, 200, 150, 25);
        this.add(txtTel);

        //Butonlar
        musteriEk = new JButton("Müşteri Ekle");
        musteriEk.setBounds(200, 310, 150, 30);
        this.add(musteriEk);
        musteriEk.setActionCommand("musteriEk");

        musteriBil = new JButton("Müşteri Bilgileri");
        musteriBil.setBounds(50, 350, 150, 30);
        this.add(musteriBil);
        musteriBil.setActionCommand("musteriBil");

        hesapBil = new JButton("Hesap Bilgileri");
        hesapBil.setBounds(200, 350, 150, 30);
        this.add(hesapBil);
        hesapBil.setActionCommand("hesapBil");

        krediBil = new JButton("Kredi Bilgileri");
        krediBil.setBounds(350, 350, 150, 30);
        this.add(krediBil);
        krediBil.setActionCommand("krediBil");

        /*Son kaydedilen müşterinin verilerini göstermesi amaçlı 
        görünmez olarak yaratılan labellar*/
        kntrlAd = new JLabel("Adı:");
        kntrlAd.setBounds(40, 400, 200, 50);
        kntrlAd.setVisible(false);
        this.add(kntrlAd);
        kntrlSoy = new JLabel("Soyadı:");
        kntrlSoy.setBounds(40, 450, 200, 50);
        kntrlSoy.setVisible(false);
        this.add(kntrlSoy);
        kntrlMail = new JLabel("Mail:");
        kntrlMail.setBounds(40, 500, 200, 50);
        kntrlMail.setVisible(false);
        this.add(kntrlMail);
        kntrlTel = new JLabel("Telefon:");
        kntrlTel.setBounds(40, 560, 200, 50);
        kntrlTel.setVisible(false);
        this.add(kntrlTel);

        musteriEk.addActionListener(this);
        musteriBil.addActionListener(this);
        hesapBil.addActionListener(this);
        krediBil.addActionListener(this);
        this.setSize(900, 700);
        this.setVisible(true);

    }
    Musteri yenimus;

    public void actionPerformed(ActionEvent e) {
        String komut = e.getActionCommand();
        //Switch case yapısı kullanarak hangi butondan komut geldiğini kontrol ediyorum
        switch (komut) {
            case "musteriEk":
                //TextFieldların içindeki veriler alınarak müşteri nesnesi oluşturuyorum
                String ad = txtAd.getText();
                String soyad = txtSoy.getText();
                String mail = txtMail.getText();
                long tel = Long.parseLong(txtTel.getText());
                yenimus = new Musteri(ad, soyad, mail, tel);

                break;
            case "musteriBil":
                /*Görünmez olarak oluşturduğum Labelların içeriklerini 
                müşteri verileri ile güncelliyorum*/
                kntrlAd.setText("Adı: " + yenimus.getAd());
                kntrlSoy.setText("Soyadı: " + yenimus.getSoy());
                kntrlMail.setText("Mail: " + yenimus.getMail());
                kntrlTel.setText("Telefon: " + yenimus.getTelefon());
                //İçerikleri güncelledikten sonra görünür duruma geçiriyorum.
                kntrlAd.setVisible(true);
                kntrlSoy.setVisible(true);
                kntrlMail.setVisible(true);
                kntrlTel.setVisible(true);
                break;
            case "hesapBil":
                
                System.out.println("hesapBil");
                break;
            case "krediBil":
                System.out.println("krediBil");
                break;
            default:
                //Switchcasedefault
        }
    }
}
