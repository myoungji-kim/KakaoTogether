package com.controller.fund;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dto.board.BoardDTO;
import com.dto.donation.CommentsDTO;
import com.service.board.BoardService;
import com.service.board.BoardServiceImpl;
import com.service.cheer.CheerService;
import com.service.cheer.CheerServiceImpl;
import com.service.donation.DonationService;
import com.service.donation.DonationServiceImpl;


@WebServlet("/fund/now/content")
public class FundNowContentServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String next = "";
		String idx = request.getParameter("idx");
		
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("idx", idx);
		map.put("cate", "fund");
		
		BoardService service = new BoardServiceImpl();
		CheerService cService = new CheerServiceImpl();
		DonationService dService = new DonationServiceImpl();
		
		try {
			BoardDTO dto = service.selectNowContent(map);
			String subtopic = service.selectContentTag(dto.getSubtopic()); // 주제 가져오기 (ex. 어린이, 여성, ..)
			List<CommentsDTO> commentsList = dService.selectComments(map); // 댓글 가져오기
			int commentsNum = dService.countComments(map);
			
			request.setAttribute("content", dto);
			request.setAttribute("subtopic", subtopic);
			request.setAttribute("commentsList", commentsList);
			request.setAttribute("commentsNum", commentsNum);
			
			int cheer = cService.selectCheer(Integer.parseInt(idx));
			request.setAttribute("cheer", cheer);
			
			next = "/fundraising/nowContent.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			next = "/Error500";
		}
		
		// main.jsp에 위임
		request.getRequestDispatcher(next).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
