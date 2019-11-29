package com.casic.client.controller;


import com.casic.client.common.job.BaseJob;
import com.casic.client.common.service.WebSocketService;
import com.casic.client.common.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "测试Controller")
public class HelloController {

    //加入Qulifier注解，通过名称注入bean
    @Autowired @Qualifier("Scheduler")
    private Scheduler scheduler;

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/hello")
    @ApiOperation(value="测试方法", notes="测试方法")
    public String say(){
        System.out.println("-------------");
        return "hello world!!";
    }

    @PostMapping("/addjob")
    @ApiOperation(value="job方法", notes="job方法")
    public String addjob(@RequestParam(value="jobClassName")String jobClassName,
                       @RequestParam(value="jobGroupName")String jobGroupName,
                       @RequestParam(value="cronExpression")String cronExpression) throws Exception
    {
        addJob(jobClassName, jobGroupName, cronExpression);
        return "this jon do successful";
    }

    public void addJob(String jobClassName, String jobGroupName, String cronExpression)throws Exception{

        // 启动调度器
        scheduler.start();

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).build();

        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
                .withSchedule(scheduleBuilder).build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            System.out.println("创建定时任务失败"+e);
            throw new Exception("创建定时任务失败");
        }
    }
    public static BaseJob getClass(String classname) throws Exception
    {
        Class<?> class1 = Class.forName(classname);
        return (BaseJob)class1.newInstance();
    }

    @Autowired
    private WebSocketService ws;

    //http://localhost:8080/Welcome1
    @RequestMapping("/Welcome1")
    @ResponseBody
    public String say2()throws Exception
    {
        ws.sendMessage();
        return "is ok";
    }
    /**
     * 按段落解析一个word文档
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value="解析word文档", notes="按段落解析word文档")
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map uploadFile(@RequestParam(value = "file", required = true) MultipartFile file) {
        String textFileName = file.getOriginalFilename();
        Map wordMap = new LinkedHashMap();//创建一个map对象存放word中的内容
        try {
            if (textFileName.endsWith(".doc")) { //判断文件格式
                InputStream fis = file.getInputStream();
                WordExtractor wordExtractor = new WordExtractor(fis);//使用HWPF组件中WordExtractor类从Word文档中提取文本或段落
                int i = 1;
                for (String words : wordExtractor.getParagraphText()) {//获取段落内容
                    System.out.println(words);
                    wordMap.put("DOC文档，第（" + i + "）段内容", words);
                    i++;
                }
                fis.close();
            }
            if (textFileName.endsWith(".docx")) {
                File uFile = new File("tempFile.docx");//创建一个临时文件
                if (!uFile.exists()) {
                    uFile.createNewFile();
                }
                FileCopyUtils.copy(file.getBytes(), uFile);//复制文件内容
                OPCPackage opcPackage = POIXMLDocument.openPackage("tempFile.docx");//包含所有POI OOXML文档类的通用功能，打开一个文件包。
                XWPFDocument document = new XWPFDocument(opcPackage);//使用XWPF组件XWPFDocument类获取文档内容
                List<XWPFParagraph> paras = document.getParagraphs();
                int i = 1;
                for (XWPFParagraph paragraph : paras) {
                    String words = paragraph.getText();
                    System.out.println(words);
                    wordMap.put("DOCX文档，第（" + i + "）段内容", words);
                    i++;
                }
                uFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(wordMap);
        return wordMap;
    }
}
