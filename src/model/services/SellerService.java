package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {

	private SellerDao sellerDAO = DaoFactory.createSellerDao();
	
	public List<Seller> findAll(){
		/*
		List<Seller> listDeps = new ArrayList<>();
		//Mock the data of list seller for now
		listDeps.add(new Seller(1, "Books"));
		listDeps.add(new Seller(2, "Computers"));
		listDeps.add(new Seller(3, "Phones and gadgets"));
		*/
		return sellerDAO.findAll();
		
	}
	
	public void saveOrUpdate(Seller seller) {
		if(seller.getId() == null) {
			sellerDAO.insert(seller);
		}
		sellerDAO.update(seller);
	}
	
	public void deleteSeller(Seller seller) {
		sellerDAO.deleteById(seller.getId());
	}
	
			
}
