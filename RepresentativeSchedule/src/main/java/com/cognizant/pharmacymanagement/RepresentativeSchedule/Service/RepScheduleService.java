package com.cognizant.pharmacymanagement.RepresentativeSchedule.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.pharmacymanagement.RepresentativeSchedule.Model.AuthResponse;
import com.cognizant.pharmacymanagement.RepresentativeSchedule.Model.Doctor;
import com.cognizant.pharmacymanagement.RepresentativeSchedule.Model.RepSchedule;
import com.cognizant.pharmacymanagement.RepresentativeSchedule.feignClient.AuthClient;
import com.cognizant.pharmacymanagement.RepresentativeSchedule.feignClient.StockFeignClient;

@Service
public class RepScheduleService {
	
	private static List<RepSchedule> details=new ArrayList<RepSchedule>();
	private static int detCount;
	
	@Autowired
	DoctorService doctors;
	
	@Autowired
	RepService reps;
	
	@Autowired
	private AuthClient authClient;
	
	@Autowired
	private StockFeignClient stockfeignclient;
	
	private static List<String> meds=new ArrayList<String>();
	private static Doctor doc=new Doctor();
	
	@SuppressWarnings("deprecation")
	public List<RepSchedule> returnschedule(String d) throws ParseException
	{
		Date date=StringtoDate(d);
		details.clear();
		detCount=0;
		reps.initialMark();
		while(detCount<doctors.getDocCount()) {
			if(date.getDay()!=0) {
				doc=doctors.retrieveDoc();
				//meds=findMeds(doc.getTreating());
				meds=stockfeignclient.getMedicineByTreatingAilment(doc.getTreating());
				if(meds==null) {
					doc=doctors.retrieveDoc(doc.getName());
				}
				else {
					doctors.setAllotment(doc.getName());
				}
				System.out.println(doc);
				
				details.add(new RepSchedule(reps.retrieveRep(),doc.getName(),doc.getTreating(),meds,doc.getSlot(),date,doc.getNumber()));   
				date=findNextDay(date);
				detCount++;
			}
			else {
				date=findNextDay(date);
			}
		}
		doctors.resetAllotment();
		//System.out.println(details);
		return details;
	}
	
	/*//Method to accept data from Database through Shivank's microservice
	//To convert JSON format to object format, use @RequestBody in parameter-list
	public List<String> findMeds(String Ailment){
		List<String> s=new ArrayList<String>();
		s.add("Orthoherb");
		s.add("Cholecalciferol");
		return s;
	}*/
	
	public Date StringtoDate(String str) throws ParseException {
		//Date date=new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String d=new String();
		d=str.substring(8, 10)+str.substring(4, 8)+str.substring(0, 4);
		return formatter.parse(d);
	}
	
	public static Date findNextDay(Date date)
	{
		final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
	    return new Date(date.getTime() + MILLIS_IN_A_DAY);
	}

	public Boolean isSessionValid(String token) {
		try {
			AuthResponse authResponse = authClient.getValidity(token);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
}
