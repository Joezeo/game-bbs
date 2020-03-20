package com.joezeo.joefgame.manager.controller;

import com.joezeo.joefgame.common.dto.JsonResult;
import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.exception.CustomizeException;
import com.joezeo.joefgame.common.dto.ProcessDTO;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RestController
@RequestMapping("/manager/process")
@Slf4j
public class ProcessController {

    @Autowired
    private RepositoryService repositoryService;

    @PostMapping("/list")
    public JsonResult<PaginationDTO> getProcesses(@RequestParam(name = "page",defaultValue = "1") Integer page,
                                                  @RequestParam(name = "size", defaultValue = "5") Integer size){
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalCount = repositoryService.createProcessDefinitionQuery().latestVersion().list().size();
        paginationDTO.setPagination(page, size, totalCount);
        page = paginationDTO.getPage();
        int index = (page - 1) * size;

        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().latestVersion().listPage(index, size);
        List<ProcessDTO> processDTOS = new ArrayList<>();
        list.stream().forEach(process -> {
            ProcessDTO pro = new ProcessDTO();
            pro.setId(process.getId());
            pro.setName(process.getName());
            pro.setVersion(process.getVersion());
            pro.setKey(process.getKey());
            processDTOS.add(pro);
        });

        paginationDTO.setDatas(processDTOS);

        return JsonResult.okOf(paginationDTO);
    }

    @PostMapping("/uploadProcess")
    public JsonResult<?> uploadProcess(HttpServletRequest request){
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest)request;
        MultipartFile mulFile = mulReq.getFile("processBpmn");

        String originalFilename = mulFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        if(!"bpmn".equals(suffix)){
            return JsonResult.errorOf(CustomizeErrorCode.ONLY_ACCEPT_BPMN);
        }

        try {
            repositoryService.createDeployment().addInputStream(originalFilename, mulFile.getInputStream()).deploy();
        } catch (IOException e) {
            log.error("部署流程文件错误：IOException，stackTrace："+e.getStackTrace());
            return JsonResult.errorOf(CustomizeErrorCode.DEPLOY_PROCESS_ERROR);
        }

        return JsonResult.okOf(null);
    }

    @PostMapping("/delete")
    public JsonResult<?> delete(@RequestBody ProcessDTO processDTO){
        try{
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDTO.getId()).singleResult();
            repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
        } catch (Exception e) {
            log.error("删除流程定义失败：stackTrace: " + e.getStackTrace());
            return JsonResult.errorOf(CustomizeErrorCode.DELETE_PROCESS_ERROR);
        }
        return JsonResult.okOf(null);
    }

    @GetMapping("/viewProcessPic")
    public void viewProcessPic(String id, HttpServletResponse response){
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
            InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());

            ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.copy(resourceAsStream, outputStream);
        } catch (IOException e) {
            log.error("获取流程定义图片失败：stackTrace: " + e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.GET_PROCESS_PIC_FALIED);
        }
    }
}
