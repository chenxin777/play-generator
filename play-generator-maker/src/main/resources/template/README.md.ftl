<#macro generateConfig indent modelInfo>
${indent}${modelInfo.fieldName}

${indent}类型: ${modelInfo.type}

${indent}描述: ${modelInfo.description}

${indent}默认值: ${modelInfo.defaultValue?c}

${indent}缩写: <#if modelInfo.abbr ??>-${modelInfo.abbr}<#else>无</#if>
-----------------------------------------
</#macro>

# ${name}

> ${description}
>
> 作者: ${author}
>
> 基于 [玩物志出品](https://github.com/chenxin777) 的 [Play代码生成器](https://github.com/chenxin777/play-generator) 制作

可以通过命令行交互式输入的方式动态生成想要的项目代码

## 使用说明

```
generate <命令> <选项参数>
```

## 参数说明

<#list modelConfig.models as modelInfo>
<#if modelInfo.groupKey ??>
<#list modelInfo.models as subModelInfo>
<@generateConfig indent="" modelInfo=subModelInfo/>
</#list>
<#else>
<@generateConfig indent="" modelInfo=modelInfo/>
</#if>

</#list>

