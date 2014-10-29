package id.co.skyforce.shop.controller;

import id.co.skyforce.shop.model.Customer;
import id.co.skyforce.shop.model.Order;
import id.co.skyforce.shop.model.OrderDetail;
import id.co.skyforce.shop.model.Product;
import id.co.skyforce.shop.service.ShoppingCartService;
import id.co.skyforce.shop.util.HibernateUtil;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * 
 * @author Irwansyah Hazniel
 *
 */
@ManagedBean
@SessionScoped
public class ShoppingCartController implements Serializable {


	// pass dari loginController
	private Customer customer;

	@ManagedProperty(value="#{loginController}")
	private LoginController loginController;

	private Product product;
	private Product produtctEdit;
	private Long qty;
	private Product productDelete;
	private Integer totalItem = 0;
	private Long productId;
	private BigDecimal totalAmount;
	private Long dummyLong;
	private Map<Product, Long> productsAndQuantity = new HashMap<>();

	public void incrementTotalItem() {
		totalItem += 1;
		calculateTotalAmount();
	}

	public Set<Entry<Product, Long>> getItems(){
		return productsAndQuantity.entrySet();
	}

	public void addProduct(Product product) {

		// check apakah product sudah ada di map
		long incrementQuantity = productsAndQuantity.containsKey(product) ? productsAndQuantity.get(product) : 0;

		// add product to map product
		productsAndQuantity.put(product, incrementQuantity + 1);

	}

	public void updateProduct(){
		//		long incrementQuantity = productsAndQuantity.get(product);
		//		String value = FacesContext.getCurrentInstance().
		//				getExternalContext().getRequestParameterMap().get("idProduct");
		//		String IdCustomer=  FacesContext.getCurrentInstance().
		//				getExternalContext().getRequestParameterMap().get("idProduct");
		//		Long customerId = Long.parseLong(value);

		//				String qtyValue = FacesContext.getCurrentInstance().
		//						getExternalContext().getRequestParameterMap().get("qty");
		//				Long qty = Long.valueOf(qtyValue);

		//Product p = productsAndQuantity. 
		//		for ( Entry<Product, Long> entry : productsAndQuantity.entrySet()){
		//			if(entry.getKey().getId()==customerId){
		//				Product p = entry.getKey();
		//				productsAndQuantity.put(p, qty);
		//				break;
		//			}
		//		}
		productsAndQuantity.put(produtctEdit, qty);
		//		productsAndQuantity.values().
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

		try {

			externalContext.redirect("login.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteProduct(){
	/*	String id1=  FacesContext.getCurrentInstance().
				getExternalContext().getRequestParameterMap().get("idp");
		Long id2 = Long.parseLong(id1);
		Product p = new Product();
		for ( Entry<Product, Long> entry : productsAndQuantity.entrySet()){
			if(entry.getKey().getId()==id2){
				p = entry.getKey();
			}
			}
			*/
		ShoppingCartService cartService = new ShoppingCartService();
//		long x = 0 ;
//		int p = (int) x;
//		productsAndQuantity.get(productDelete);
//		totalItem = (int) (totalItem - productsAndQuantity.values());
			productsAndQuantity.remove(productDelete);
			totalAmount = cartService.totalAmountService(productsAndQuantity);
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

			try {

				externalContext.redirect("cart.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}

	public void calculateTotalAmount() {

		ShoppingCartService cartService = new ShoppingCartService();
		totalAmount = cartService.totalAmountService(productsAndQuantity);

	}

	public Product getProdutctEdit() {
		return produtctEdit;
	}

	public void setProdutctEdit(Product produtctEdit) {
		this.produtctEdit = produtctEdit;
	}

	public Product getProductDelete() {
		return productDelete;
	}

	public void setProductDelete(Product productDelete) {
		this.productDelete = productDelete;
	}

	public String checkout() {

		customer = loginController.cust;
		if (customer.getEmail() == null) {
			return "/user/login.xhtml";
		}
		return "checkout";

	}

	public String prosesBeli() {

		Session session = HibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();

		Order order = new Order();

		order.setDate(new Date());
		order.setCustomer(customer);
		order.setTotalAmount(totalAmount);

		for(Entry<Product, Long> e : productsAndQuantity.entrySet()) {

			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setProduct(e.getKey());
			orderDetail.setQuantity(e.getValue().intValue());
			orderDetail.setPrice(e.getKey().getPrice());
			order.getOrderDetails().add(orderDetail);
			session.save(orderDetail);

		}

		session.save(order);

		transaction.commit();
		session.close();

		// clear object's contents
		totalItem = 0;
		totalAmount = new BigDecimal(0);
		productsAndQuantity = new HashMap<>();

		return "successtransaction";

	}

	public String cancel() {
		return "index";
	}

	public Integer getTotalItem() {
		return totalItem;
	}

	public void setTotalItem(Integer totalItem) {
		this.totalItem = totalItem;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getProductId() {
		return productId;
	}

	public Long getQty() {
		return qty;
	}

	public Long getDummyLong() {
		return dummyLong;
	}

	public void setDummyLong(Long dummyLong) {
		this.dummyLong = dummyLong;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Map<Product, Long> getProductsAndQuantity() {
		return productsAndQuantity;
	}

	public void setProductsAndQuantity(Map<Product, Long> productsAndQuantity) {
		this.productsAndQuantity = productsAndQuantity;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LoginController getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

}
