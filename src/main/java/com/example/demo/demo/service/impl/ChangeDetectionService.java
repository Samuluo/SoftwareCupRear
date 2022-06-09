package com.example.demo.demo.service.impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjh
 * @since 2022-05-31
 */
@Slf4j
@Service
public class ChangeDetectionService {
    public void changeDetection(String path1, String path2, String result_path){
        //前面一半是本地环境下的python的启动文件地址，后面一半是要执行的python脚本地址
        String[] arguments = new String[] {"D:/anaconda3/python.exe", "python/changeDetection/test.py",path1,path2,result_path};
//        String[] arguments = new String[] {"python/changeDetection/dist/test.exe",path1,path2,result_path};
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(arguments);
            // 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            //waitFor是用来显示脚本是否运行成功，1表示失败，0表示成功，还有其他的表示其他错误
            int re = proc.waitFor();
            System.out.println(re);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
