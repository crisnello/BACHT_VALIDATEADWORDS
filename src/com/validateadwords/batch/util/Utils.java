package com.validateadwords.batch.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Utils {
	
	
	/**
	 * calcula o intervalo de dias entre duas datas, nao importa a ordem 
	 * o resultado sempre sera positivo
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int intervaloDias(Date dataDe, Date dataAte){
		
		Calendar calAte = Calendar.getInstance();
		calAte.setTime(dataAte);
		
		Calendar calDe = Calendar.getInstance();
		calDe.setTime(dataDe);
		
		int dif = calAte.get(Calendar.DAY_OF_YEAR) - calDe.get(Calendar.DAY_OF_YEAR);
		
		return Math.abs(dif);
		
	}
	
	
	
}
