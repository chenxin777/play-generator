package com.chenxin.acm;

import java.util.Scanner;

/**
 * ACM 输入模版
 * @author ${mainTemplate.author!''}
 */
public class MainTemplate {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

<#if loop>
        while (scanner.hasNext()) {
</#if>
            // 输入元素个数
            int n = scanner.nextInt();

            // 读取数组
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }

            int sum = 0;
            for (int num : arr) {
                sum += num;
            }

            System.out.println("${mainTemplate.outputText!'sum = '}" + sum);
<#if loop>
        }
</#if>
        scanner.close();
    }

}
