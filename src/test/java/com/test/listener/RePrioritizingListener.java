package com.test.listener;

import org.testng.IAlterSuiteListener;
import org.testng.IAnnotationTransformer;
import org.testng.Reporter;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 配置testng的执行顺序
 *
 * @author：关河九州
 * @date：2019/12/5 19:17
 * @version：1.0
 */
public class RePrioritizingListener implements IAnnotationTransformer {
    HashMap<Object, Integer> priorityMap = new HashMap<Object, Integer>();
    Integer class_priorityCounter = 10000;
    // The length of the final priority assigned to each method.
    Integer max_testpriorityLength = 4;

    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {


        // 测试方法的类
        Class<?> declaringClass = testMethod.getDeclaringClass();
        // 在测试方法上分配的测试的当前优先级
        Integer test_priority = annotation.getPriority();
        // 当前类优先
        Integer current_ClassPriority = priorityMap.get(declaringClass);

        if (current_ClassPriority == null) {
            current_ClassPriority = class_priorityCounter++;
            priorityMap.put(declaringClass, current_ClassPriority);
        }

        String concatenatedPriority = test_priority.toString();

        // 这个数字的开头加0
        while (concatenatedPriority.length() < max_testpriorityLength) {
            concatenatedPriority = "0" + concatenatedPriority;
        }

        // 将我们的类计数器连接到测试级优先级
        //例如优先级为1的测试：1000100001；与a相同的测试类
        //优先级2：1000100002，优先级为1的下一个类1000200001
        concatenatedPriority = current_ClassPriority.toString() + concatenatedPriority;

        //设置测试方法的新优先级
        annotation.setPriority(Integer.parseInt(concatenatedPriority));

        String printText = testMethod.getName() + " Priority = " + concatenatedPriority;
        Reporter.log(printText);
        System.out.println(printText);
    }
}
