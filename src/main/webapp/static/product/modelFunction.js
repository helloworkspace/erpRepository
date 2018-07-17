// 商品规格模块 
	
	//弹出一个窗口，是那个选择规格的总窗口
	function openChooseModelDialog(){
		$("#modelDialog").dialog("open").dialog("setTitle","规格");
	}
	
	//添加规格信息
	function openModelAddDialog(){
		$("#addModelDialog").dialog("open").dialog("setTitle","添加规格信息");
	}
	
	//删除选中的规格
	function deleteModel(){
		var selectedRows=$("#modelTable").datagrid("getSelections");
		if(selectedRows.length!=1){
			 $.messager.alert("系统提示","请选择要删除的数据！");
			 return;
		 }
		 var id=selectedRows[0].id;
		 $.messager.confirm("系统提示","您确定要删除这条数据吗？",function(r){
				if(r){
					$.post("/admin/model/delete",{id:id},function(result){
						if(result.success){
							 $("#modelTable").datagrid("reload");
						}else{
							$.messager.alert("系统提示",result.errorInfo);
						}
					},"json");
				} 
	   });
	}
	
	//选择好规格时，点确定
	function chooseModel(){
		var selectedRows=$("#modelTable").datagrid("getSelections");
		if(selectedRows.length!=1){
			 $.messager.alert("系统提示","请选择规格！");
			 return;
		}
		var name=selectedRows[0].name;
		$("#model").combobox("reload");
		$("#model").combobox("setValue",name);
		$("#modelDialog").dialog("close");
	}
	
	//关闭整个选择窗口
	function closeModelDialog(){
		$("#modelDialog").dialog("close");
	}
	
	
	//保存添加的规格
	function saveModel(){
		$("#addModelText").form("submit",{
			url:"/admin/model/save",
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统提示","保存成功！");
					closeModelAddDialog();
					$("#modelTable").datagrid("reload");
				}else{
					$.messager.alert("系统提示",result.errorInfo);
				}
			}
		 });
	}
	
	//关闭添加新规格的窗口，并且设置窗口规格信息为空
	function closeModelAddDialog(){
		$("#addModelDialog").dialog("close");
		$("#modelName").val("");
	}

	/*
	*$(function(){}); 是$(document).ready()的简写 
	*/
	/*
	$(function (){
		//window.alert("lalla");
		 //双击规格展示表，就是双击名称，关闭规格对话窗口，自动填充
		 $("#modelTable").datagrid({  
		        //双击事件  //双击选中规格
		        onDblClickRow: function (index, row) {  
		        	var selectedRows=$("#modelTable").datagrid("getSelections");
		    		var name=selectedRows[0].name;
		    		$("#model").combobox("reload");
		    		$("#model").combobox("setValue",name);
		    		$("#modelDialog").dialog("close");
		        }  
		    });  		 
	});*/