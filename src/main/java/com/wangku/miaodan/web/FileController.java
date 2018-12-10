package com.wangku.miaodan.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wangku.miaodan.core.model.Order;
import com.wangku.miaodan.core.service.IOrderService;

@Controller
@RequestMapping("/file")
public class FileController {
	
	@Autowired
	private IOrderService orderService;
	
	@RequestMapping("/upload")
	@ResponseBody
	public Map<String, Object> upload(MultipartFile file, HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("/user-info");
		Map<String, Object> result = new HashMap<String, Object>();
		if (file != null) {
			String name = file.getOriginalFilename();
			name = new StringBuffer(40).append(UUID.randomUUID().toString().replace("-", "")).append(".").append(name).toString();
			File temp = new File(path + File.separator + name);
			try {
				temp.mkdirs();
				file.transferTo(temp);
				result.put("code", 200);
				result.put("url", "/user-info/" + name);
			} catch (IOException e) {
				e.printStackTrace();
				result.put("code", 500);
			}
		}
		return result;
	}
	
	@RequestMapping("/uploadData")
	@ResponseBody
	public Object uploadData() {
		try {
			List<Order> list = new ArrayList<Order>(52);
			XSSFWorkbook book = new XSSFWorkbook(new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\样品1.xlsx")));
			XSSFSheet sheet = book.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				XSSFRow row = sheet.getRow(i);
				if (row == null || i >= 53)
					continue;
				String name = getString(row.getCell(0));
				String mobile = getString(row.getCell(1));
				String city = getString(row.getCell(2));
				String age = getString(row.getCell(3));
				String vocation = getString(row.getCell(4));
				String workTime = getString(row.getCell(5));
				String sex = getString(row.getCell(6));
				String monthIncome = getString(row.getCell(7));
				String payType = getString(row.getCell(8));
				String sum = getString(row.getCell(9));
				String credit = getString(row.getCell(10));
				String car = getString(row.getCell(11));
				String building = getString(row.getCell(12));
				String gjj = getString(row.getCell(13));
				String sb = getString(row.getCell(14));
				String sx = getString(row.getCell(15));
				String identity = getString(row.getCell(16));
				String zmf = getString(row.getCell(17));
				String wld = getString(row.getCell(18));
				
				Order order = new Order();
				order.setName(name);
				order.setMobile(mobile);
				order.setCity(city);
				order.setAge(Integer.parseInt(age));
				order.setVocation("上班族".equals(vocation)?(byte)0:"个体户".equals(vocation)?(byte)1:(byte)2);
				order.setWorkTime("1年".equals(workTime)?(byte)0:"1-3年".equals(workTime)?(byte)1:"3-5年".equals(workTime)?(byte)2:(byte)3);
				order.setSex("男".equals(sex)?(byte)0:(byte)1);
				int monthIncomeInt = 0;
				try {
					monthIncomeInt = Integer.parseInt(monthIncome);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				
				if (monthIncomeInt < 3000) {
					order.setMonthIncome((byte)0);
				} else if (monthIncomeInt <= 5000 ) {
					order.setMonthIncome((byte)1);
				} else if (monthIncomeInt <= 8000 ) {
					order.setMonthIncome((byte)2);
				} else if (monthIncomeInt <= 10000 ) {
					order.setMonthIncome((byte)3);
				} else {
					order.setMonthIncome((byte)4);
				}
				
				order.setWagesType("银行代发".equals(payType)?(byte)0:(byte)1);
				order.setSum(new BigDecimal(Long.parseLong(sum)));
				order.setCreditCard("有".equals(credit)?(byte)1:(byte)0);
				order.setCar("有".equals(car)?(byte)1:(byte)0);
				order.setBuilding("有".equals(building)?(byte)1:(byte)0);
				order.setAccumulationFund("有".equals(gjj)?(byte)1:(byte)0);
				order.setSocialInsurance("有".equals(sb)?(byte)1:(byte)0);
				order.setLifeInsurance("有".equals(sx)?(byte)1:(byte)0);
				order.setIdentyNumber(identity);
				order.setSesameCredit(Integer.parseInt(zmf));
				order.setWeiLiDai("有".equals(wld)?(byte)1:(byte)0);
				order.setApplyTime(new Date());
				list.add(order);
			}
			
			orderService.saveBatch(list);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ok";
	}
	
	public String getString(XSSFCell cell) {
		String val = "0";
		if (!Objects.isNull(cell)) {
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				val = String.valueOf((long)cell.getNumericCellValue());
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				val = cell.getStringCellValue();
			}
		} 
		return val;
	}

}
