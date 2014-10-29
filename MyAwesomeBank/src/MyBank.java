import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.codequ.util.HibernateUtil;

import id.web.skyforce.bank.model.Account;
import id.web.skyforce.bank.model.Address;
import id.web.skyforce.bank.model.Customer;


public class MyBank {
	public static void simpanCustomer(Customer customer){
		Session session = HibernateUtil.openSession();
		Transaction trx = session.beginTransaction();
		session.save(customer);
		trx.commit();
		session.close();
	}
	public static void simpanAlamat(Address address){
		Session session = HibernateUtil.openSession();
		Transaction trx = session.beginTransaction();
		session.save(address);
		trx.commit();
		session.close();
	}
	
	
	public static void savingAccount(Account account){
		Session session = HibernateUtil.openSession();
		Transaction trx = session.beginTransaction();
		session.save(account);
		trx.commit();
		session.close();
	}

	public static void main(String[] args) {
		SimpleDateFormat formatTanggal =new SimpleDateFormat("dd-MM-yyyy");
		Customer bg = new Customer();
		bg.setFirstName("Bill");
		bg.setLastName("Gates");
		bg.setGender('M');
		String date ="1955-10-28";
		Date birthDate = null;
		try {
			birthDate = formatTanggal.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bg.setBirthDate(birthDate);
		bg.setIdNumber("12345678");
		simpanCustomer(bg);

		Address address = new Address();
		address.setCity("San Francisco");
		address.setPostalCode("94027");
		address.setStreet("Silicon Valley No.13");
		address.setCustomer(bg);
		simpanAlamat(address);
		
		
		Account account = new Account();
		account.setBalance(new BigDecimal(40_000_000));
		account.setStatus('A');
		account.setAccountNo("XYZ123");
		account.setCustomer(bg);
		savingAccount(account);
		
		Account account2 = new Account();
		account2.setBalance(new BigDecimal(40_000_000));
		account2.setStatus('I');
		account2.setAccountNo("XYZ456");
		account2.setCustomer(bg);
		savingAccount(account2);
		
		
	}
	
}
