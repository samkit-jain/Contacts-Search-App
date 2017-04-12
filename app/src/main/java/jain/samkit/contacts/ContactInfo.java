package jain.samkit.contacts;

public class ContactInfo {
    String id, name, number, email, organization, address, note, im;

    public ContactInfo(String id, String name, String number, String email, String organization, String address, String note, String im) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.email = email;
        this.organization = organization;
        this.address = address;
        this.note = note;
        this.im = im;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getOrganization() {
        return organization;
    }

    public String getAddress() {
        return address;
    }

    public String getNote() {
        return note;
    }

    public String getIm() {
        return im;
    }
}
