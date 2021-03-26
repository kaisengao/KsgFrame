package com.kaisengao.base.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: ReloadAnnotations
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/22 13:43
 * @Description: LoadSir 重试注解
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReloadAnnotations {

}
