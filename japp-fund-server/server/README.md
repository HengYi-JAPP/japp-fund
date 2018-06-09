[TOC]
# japp-fund-server api
apiBaseUrl = http://task.hengyi.com:8080/fund-server/api
## token权限
在header里添加jwtToken
> Authorization： Bearer {token}

***注意: Bearer和token之间有个空格***
在所有

## api列表
|url|method|描述|备注|
|:------------------------------------|:---:|:---:|:---:|
|/auth/jwtToken?appId=japp-fund-client&appSecret=123456&cas&oaId       | get | 获取token |***前三项为必输，cas的principal***，oaId可选，但最好也一起传，测试就用cas=admin，可以操作所有权限 |
|/exports/{year}/{month}?token=       | get | 导出每日动态 xlsx | |
|/exports/sumReports/{year}/{month}?token=       | get | 导出合并余额 xlsx | |
|[/baseData/corporations](#公司)         | post | 创建公司 | |
|[/baseData/corporations/{id}](#公司)| put | 更新公司|输入和返回类似创建，id 必输 |
|/baseData/corporations/{id}| delete | 删除公司 | |
|/corporations/{id}| get | 获取公司 | |
|/corporations| get | 获取所有公司 | |
|/currencies| | 币种 | 和公司类似，把**corporations**换成**currencies** |
|/purposes| | 项目用途 | 和公司类似，把**corporations**换成**purposes** |
|[/baseData/operatorGroups](#用户组数据) |  | 用户组，数据输入先参考[权限](#权限数据) | 和公司类似，把**corporations**换成**operatorGroups** |
|[/baseData/operators](#用户导入数据) | post | 用户导入 |
|[/baseData/operators](#用户数据) | get | 用户获取 |
|[/baseData/operators/{id}/permission](#用户权限数据) | put | 创建或修改用户权限，数据输入先参考[权限](#权限数据) |
|[/baseData/operators/{id}/permission](#用户权限数据) | get | 获取用户权限 |
|[/monthFundPlans](#月度计划)| post | 创建月度计划 ||
|[/monthFundPlans/{id}](#月度计划)| put | 更新月度计划|输入和返回类似创建，id 必输 |
|/monthFundPlans/{id}| get | 获取月度计划 | |
|/monthFundPlans/{id}| delete | 删除月度计划 | |
|/monthFundPlans?corporationId&currencyId&year&month| get | 获取所有月度计划 | |
|[/monthFundPlans/{monthFundPlanId}/dayFundPlans](#日计划)| post | 创建日计划 | **需要在一个已知月度计划下创建** |
|/dayFundPlans/{id}| put | 更新日计划 | |
|/dayFundPlans/{id}| get | 获取日计划 | |
|/dayFundPlans/{id}| delete | 删除日计划| |
|/dayFundPlans?corporationId&currencyId&date| get | 获取所有日计划 | |
|[/monthFundPlans/{monthFundPlanId}/funds](#日执行)| post | 创建日执行 | **需要在一个已知月度计划下创建** |
|[/dayFundPlans/{dayFundPlanId}/funds](#日执行)| post | 创建日执行 | **需要在一个已知日计划下创建** |
|/dayFundPlans/{dayFundPlanId}/finish| put | 创建日执行 | **需要在一个已知日计划下创建** |
|/funds/{id}| put | 更新日执行 | |
|/funds/{id}| get | 获取日执行 | |
|/funds/{id}| delete | 删除日执行| |
|[/funds?corporationId&currencyId&date&year&month](#日执行)| get | 获取所有日执行 | |
|[/balances?corporationId&currencyId&date&year&month](#日余额)| get | 获取所有余额 | |
|[/reports/sumFund/?corporationId&currencyId&date&year&month](#合计日执行报表)| get | 获取合计日执行报表，某公司某段时间，按天合计支出和收入 | |
|[/reports/sumDayFundPlan/?corporationId&currencyId&date&year&month](#合计日计划报表)| get | 获取合计日计划报表，某公司某段时间，按天合计支出和收入 | |

> **提示：** corporationId可以多选，如：corporationId=1&corporationId=2...


## api描述
### 公司
>输入
``` json
{
	"name": string
}
```
>返回
``` json
{
	”id": string
	"name": string
	"creator":  {...}
	"createDateTime":  long
	"modifier":  {...}
	"modifyDateTime": long
}
```

###权限数据
> 权限角色数据 - RoleType 枚举类型，传输的时候用string就可以
``` json
{
	MONTH_PLAN_UPDATE 月计划维护
	DAY_PLAN_UPDATE   日计划维护
	FUND_UPDATE       日执行维护
	FUND_EAD          查看
}
```

> 权限对象数据 - Permission
``` json
{
	"allCorporation": boolean
	"corporations": [{id:string}]
	"allCurrency": boolean
	"currencies": [{id:string}]
	"roleTypes":[RoleType]
}
```


###用户组数据
> 输入
``` json
{
	"name": string
	"permissions": [{Permission}] Permission数组
}
```

> 输出
``` json
{
	"id": string
	"name": string
	"permissions": [{Permission}] Permission数组
	...
}
```

###用户数据
>输入
``` json
{
	"oaId": string
	"hrId": string
}
```
>返回
``` json
{
	”id": string
	"name": string
	"admin": boolean 是否管理员
	"baseDataMaintain": boolean 是否基础数据管理员
	...
}
```

###用户权限数据
>输入
``` json
{
	"operatorGroups": [ {id:string} ] 用户组数组
	"permissions": [{Permission}] Permission数组
}
```
>返回
``` json
{
	"operatorGroups": [ {OperatorGroup} ] 用户组数组
	"permissions": [{Permission}] Permission数组
}
```

### 月度计划
>输入
``` json
{
	"year": int
	"month": int
	"corporation": {“id”: string}
	"currency": {“id”: string}
	"purpose": {“id”: string}
	"money": bigdecimal
	"note": string
}
```
>返回
``` json
{
	”id": string
	"year": int
	"month": int
	"corporation":  {...}
	"currency":  {...}
	"purpose":  {...}
	"money":  bigdecimal
	"note":  string
	"creator":  {...}
	"createDateTime":  long
	"modifier": {...}
	"modifyDateTime": long
}
```

### 获取所有月度计划
>输入
``` json
{
	"year": int
	"month": int
	"corporation": {“id”: string}
	"currency": {“id”: string}
	"purpose": {“id”: string}
	"money": bigdecimal
	"note": string
}
```
>返回
``` json
[
	{
		”id": string
		"year": int
		"month": int
		"corporation":  {...}
		"currency":  {...}
		"purpose":  {...}
		"money":  bigdecimal
		"note":  string
		"creator":  {...}
		"createDateTime":  long
		"modifier": {...}
		"modifyDateTime": long
	}
	...
]
```

### 日计划
>输入
``` json
{
	"date": long | js里的date对象
	"money": bigdecimal
	"note": string
}
```
>返回
``` json
{
	”id": string
	"monthFundPlan":  {}
	"date": long
	"corporation":  {}
	"currency":  {}
	"purpose":  {}
	"money": bigdecimal
	"note": string
	"creator":  {}
	"createDateTime":  long
	"modifier":
	"modifyDateTime":
}
```

### 日执行
>返回
``` json
{
	"corporation":  {}
	"currency":  {}
	"date": long
	"money": bigdecimal,
	"note": string
}
```

### 日余额
>返回
``` json
{
	"corporation":  {}
	"currency":  {}
	"date": long
	"money": bigdecimal
}
```

### 合计日执行报表
>返回
``` json
{
	"corporation":  {}
	"currency":  {}
	"date": long
	"money": bigdecimal  （正为收入，负为支出）
}
```

### 异常处理
基本异常 
``` json
[
	{
		”errorCode": string
	}
...
]
```
如："E00000" 代表内部错误

其他
``` json
{
	"message":  string
}
```
也是数组，但没有errorCode，代表数据库异常，nullException等等，会把Exception里的getMessage拿出来，属于未知