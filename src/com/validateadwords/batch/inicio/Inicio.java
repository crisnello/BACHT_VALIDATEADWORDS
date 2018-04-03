package com.validateadwords.batch.inicio;

import java.io.File;

import com.validateadwords.batch.arquivo.AddArquivo;
import com.validateadwords.batch.dao.ArquivoDao;

public class Inicio {

	public static void main(String[] args) {
		try{
			if(args[0].trim().length() == 0){
				System.out.println("Diret??rio para monitorar n??o informado.");
				return;
			}
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Diret??rio para monitorar n??o informado!");
			return;
		}
		
		File diretorio = null;
		String [] arquivos = null;
		File f = null;
		ArquivoDao aDao = new ArquivoDao();
		while(true){
			if(aDao.allProcessado()){
				diretorio = new File(args[0]);  
				arquivos = diretorio.list();
				for(int i=0; i<arquivos.length; i++) {  
					if(arquivos[i].toLowerCase().endsWith("xml")){
						f = new File(args[0]+arquivos[i]);
						AddArquivo a = new AddArquivo();
						a.setInsert(f);
						Thread t = new Thread(a);
						t.start();
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
				}
			}else{
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
