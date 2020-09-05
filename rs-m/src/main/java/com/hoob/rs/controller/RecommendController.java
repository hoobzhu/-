package com.hoob.rs.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.comm.vo.ListResponse;
import com.hoob.rs.comm.vo.Response;
import com.hoob.rs.comm.vo.VoResponse;
import com.hoob.rs.model.ReSimilarContent;
import com.hoob.rs.model.ReTopNContent;
import com.hoob.rs.model.ReUserContent;
import com.hoob.rs.service.RecommendService;
import com.hoob.rs.utils.StringUtils;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;


@RestController
@RequestMapping
public class RecommendController {

	static Logger logger = LogManager.getLogger(RecommendController.class);

	@Resource
	private RecommendService  recommendService;




	/**
	 * 获取内容相似性
	 * @param login
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,path="/v1/resimilarcontent")
	public ListResponse<ReSimilarContent> getReSimilarContent(@RequestParam("contentid") String contentId,
			@RequestParam("begin") int begin,@RequestParam("pagesize") int pageSize,
			HttpServletRequest requestn){
		ListResponse reponse=new ListResponse<ReSimilarContent>();
		contentId=StringUtils.handleStrParam(contentId);
		QueryResult<ReSimilarContent> list=recommendService.getReSimilarContent(contentId, begin, pageSize);
		if(list!=null&&list.getResults()!=null){
			reponse.setList(list.getResults());
			reponse.setTotal(list.getCount());
		}
		return reponse;
	}
	/**
	 * 获取用户行为推荐
	 * @param login
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,path="/v1/reusercontent")
	public ListResponse<ReUserContent> getReUserContent(@RequestParam("userid") String userId,
			@RequestParam("begin") int begin,@RequestParam("pagesize") int pageSize,
			HttpServletRequest requestn){
		ListResponse reponse=new ListResponse<ReUserContent>();
		userId=StringUtils.handleStrParam(userId);
		QueryResult<ReUserContent> list=recommendService.getReUserContent(userId, begin, pageSize);
		if(list!=null&&list.getResults()!=null){
			reponse.setList(list.getResults());
			reponse.setTotal(list.getCount());
		}
		return reponse;
	}

	/**
	 * 获取热门内容推荐
	 * @param login
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,path="/v1/retopncontent")
	public ListResponse<ReTopNContent> ReUserContent(
			@RequestParam("begin") int begin,@RequestParam("pagesize") int pageSize,
			HttpServletRequest requestn){
		ListResponse reponse=new ListResponse<ReTopNContent>();
		QueryResult<ReTopNContent> list=recommendService.getReTopNContent(begin,pageSize);
		if(list!=null&&list.getResults()!=null){
			reponse.setList(list.getResults());
			reponse.setTotal(list.getCount());
		}
		return reponse;
	}

	/**
	 * 添加相似性内容推荐
	 * */
	@RequestMapping(method = RequestMethod.POST, path = "/v1/resimilarcontent/add",produces = {"application/json;charset=utf-8" })
	public Response add(@RequestBody ReSimilarContent re) {
		logger.debug("add ReSimilarContent : body={}", re);
		Response response=new Response();
		recommendService.addBaseEntity(re);
		return response;
	}
	@RequestMapping(method = RequestMethod.POST, path = "/v1/reusercontent/add",produces = {"application/json;charset=utf-8" })
	public Response add(@RequestBody ReUserContent re) {
		logger.debug("add ReUserContent : body={}", re);
		Response response=new Response();
		recommendService.addBaseEntity(re);
		return response;
	}
	@RequestMapping(method = RequestMethod.POST, path = "/v1/retopncontent/add",produces = {"application/json;charset=utf-8" })
	public Response add(@RequestBody ReTopNContent re) {
		logger.debug("add ReTopNContent : body={}", re);
		Response response=new Response();
		recommendService.addBaseEntity(re);
		return response;
	}
	@RequestMapping(method = RequestMethod.POST, path = "/v1/resimilarcontent/edit",produces = {"application/json;charset=utf-8" })
	public Response edit(@RequestBody ReSimilarContent re) {
		logger.debug("add ReSimilarContent : body={}", re);
		Response response=new Response();
		recommendService.editReSimilarContent(re);
		return response;
	}
	@RequestMapping(method = RequestMethod.POST, path = "/v1/reusercontent/edit",produces = {"application/json;charset=utf-8" })
	public Response edit(@RequestBody ReUserContent re) {
		logger.debug("update ReUserContent : body={}", re);
		Response response=new Response();
		recommendService.editReUserContent(re);
		return response;
	}
	@RequestMapping(method = RequestMethod.POST, path = "/v1/retopncontent/edit",produces = {"application/json;charset=utf-8" })
	public Response edit(@RequestBody ReTopNContent re) {
		logger.debug("update ReTopNContent : body={}", re);
		Response response=new Response();
		recommendService.editReTopNContent(re);
		return response;
	}
	
	
	@RequestMapping(method = RequestMethod.POST, path = "/v1/retopncontent/delete",produces = {"application/json;charset=utf-8" })
	public Response deleteReTopNContent(@RequestParam("id") long id) {
		logger.debug("delete ReTopNContent : id={}", id);
		Response response=new Response();
		recommendService.deleteReTopNContent(id);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/v1/reusercontent/delete",produces = {"application/json;charset=utf-8" })
	public Response deleteReUserContent(@RequestParam("id") long id) {
		logger.debug("delete ReTopNContent : id={}", id);
		Response response=new Response();
		recommendService.deleteReUserContent(id);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/v1/resimilarcontent/delete",produces = {"application/json;charset=utf-8" })
	public Response deleteReSimilarContent(@RequestParam("id") long id) {
		logger.debug("delete ReTopNContent : id={}", id);
		Response response=new Response();
		recommendService.deleteReSimilarContent(id);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/v1/retopncontent/detail",produces = {"application/json;charset=utf-8" })
	public VoResponse getReTopNContent(@RequestParam("id") long id) {
		logger.debug("get ReTopNContent : id={}", id);
		VoResponse response=new VoResponse();
		ReTopNContent db=recommendService.getReTopNContent(id);
		response.setVo(db);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/v1/reusercontent/detail",produces = {"application/json;charset=utf-8" })
	public VoResponse detailReUserContent(@RequestParam("id") long id) {
		logger.debug("get ReUserContent : id={}", id);
		VoResponse response=new VoResponse();
		ReUserContent db=recommendService.getReUserContent(id);
		response.setVo(db);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/v1/resimilarcontent/detail",produces = {"application/json;charset=utf-8" })
	public VoResponse getReSimilarContent(@RequestParam("id") long id) {
		logger.debug("get ReSimilarContent : id={}", id);
		VoResponse response=new VoResponse();
		ReSimilarContent db=recommendService.getReSimilarContent(id);
		response.setVo(db);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/v1/resimilarcontent/check",produces = {"application/json;charset=utf-8" })
	public Response checkSimilarContent(@RequestParam("contentid") String contentId) {
		logger.debug("check ReSimilarContent : contentId={}", contentId);
		Response response=new Response();
		ReSimilarContent db=recommendService.getReSimilarContentByContentId(contentId);
		if(db!=null){
			response.setResultCode(1);
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/v1/reusercontent/check",produces = {"application/json;charset=utf-8" })
	public Response checkReUserContent(@RequestParam("userid") String userId) {
		logger.debug("check ReUserContent : userId={}", userId);
		Response response=new Response();
		ReUserContent db=recommendService.getReUserContentByUserId(userId);
		if(db!=null){
			response.setResultCode(1);
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/v1/retopncontent/check",produces = {"application/json;charset=utf-8" })
	public Response checkReTopNContent(@RequestParam("contentid") String contentId) {
		logger.debug("check ReTopNContent : contentId={}", contentId);
		Response response=new Response();
		ReTopNContent db=recommendService.getReTopNContentByContentId(contentId);
		if(db!=null){
			response.setResultCode(1);
		}
		return response;
	}
}
