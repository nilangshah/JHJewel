package com.jhjewel.bill.db;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.jhjewel.bill.model.Dealer;
import com.jhjewel.util.HibernateUtil;

public class DealerDAO {
	private static final DealerDAO DEALERDAO = new DealerDAO();

	private DealerDAO() {

	}

	public static DealerDAO getInstance() {
		return DEALERDAO;
	}

	public void addDealer(Dealer dealer) {
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			trns = session.beginTransaction();
			session.save(dealer);
			trns.commit();
		} catch (RuntimeException e) {
			if (trns != null) {
				trns.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void deleteDealer(int dealerId) {
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			trns = session.beginTransaction();
			Dealer dealer = (Dealer) session.load(Dealer.class, new Integer(dealerId));
			session.delete(dealer);
			trns.commit();
		} catch (RuntimeException e) {
			if (trns != null) {
				trns.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void updateDealer(Dealer dealer) {
		Transaction trns = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			trns = session.beginTransaction();
			session.update(dealer);
			trns.commit();
		} catch (RuntimeException e) {
			if (trns != null) {
				trns.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public List<Dealer> getAllDealers() {
		List<Dealer> dealers = new ArrayList<Dealer>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			dealers = session.createQuery("from Dealer").list();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return dealers;
	}

	public Dealer getDealerById(int dealerId) {
		Dealer dealer = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String queryString = "from Dealer where dealerId = :dealerId";
			Query query = session.createQuery(queryString);
			query.setParameter("dealerId", dealerId);
			dealer = (Dealer) query.getSingleResult();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return dealer;
	}
}
