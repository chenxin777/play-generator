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
${modelInfo?index + 1}） ${modelInfo.fieldName}

类型: ${modelInfo.type}

描述: ${modelInfo.description}

默认值: ${modelInfo.defaultValue?c}

缩写: -${modelInfo.abbr}

</#list>

