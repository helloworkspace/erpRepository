// 商品品牌模块 
	
	//弹出一个窗口，是那个选择品牌的总窗口
	function openChooseBrandDialog(){
		$("#brandDialog").dialog("open").dialog("setTitle","品牌");
	}
	
	//添加品牌信息
	function openBrandAddDialog(){
		$("#addBrandDialog").dialog("open").dialog("setTitle","添加品牌信息");
	}
	
	//删除选中的品牌
	function deleteBrand(){
		var selectedRows=$("#brandTable").datagrid("getSelections");
		if(selectedRows.length!=1){
			 $.messager.alert("系统提示","请选择要删除的数据！");
			 return;
		 }
		 var id=selectedRows[0].id;
		 $.messager.confirm("系统提示","您确定要删除这条数据吗？",function(r){
				if(r){
					$.post("/admin/brand/delete",{id:id},function(result){
						if(result.success){
							 $("#brandTable").datagrid("reload");
						}else{
							$.messager.alert("系统提示",result.errorInfo);
						}
					},"json");
				} 
	   });
	}
	
	//选择好品牌时，点确定
	function chooseBrand(){
		var selectedRows=$("#brandTable").datagrid("getSelections");
		if(selectedRows.length!=1){
			 $.messager.alert("系统提示","请选择品牌！");
			 return;
		}
		var name=selectedRows[0].name;
		$("#brand").combobox("reload");
		$("#brand").combobox("setValue",name);
		
		//$("#brand2").combobox("reload");
		//$("#brand2").combobox("setValue",name);
		
		$("#brandDialog").dialog("close");
	}
	
	//关闭整个选择窗口
	function closeBrandDialog(){
		$("#brandDialog").dialog("close");
	}
	
	
	//保存添加的品牌
	function saveBrand(){
		$("#addBrandText").form("submit",{
			url:"/admin/brand/save",
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统提示","保存成功！");
					closeBrandAddDialog();
					$("#brandTable").datagrid("reload");
				}else{
					$.messager.alert("系统提示",result.errorInfo);
				}
			}
		 });
	}
	
	//关闭添加新品牌的窗口，并且设置窗口品牌信息为空
	function closeBrandAddDialog(){
		$("#addBrandDialog").dialog("close");
		$("#brandName").val("");
	}

	/*
	*$(function(){}); 是$(document).ready()的简写 
	*/
	$(function (){
		//window.alert("lalla");
		 //双击品牌展示表，就是双击名称，关闭品牌对话窗口，自动填充
		 $("#brandTable").datagrid({  
		        //双击事件  //双击选中品牌
		        onDblClickRow: function (index, row) {  
		        	var selectedRows=$("#brandTable").datagrid("getSelections");
		    		var name=selectedRows[0].name;
		    		$("#brand").combobox("reload");
		    		$("#brand").combobox("setValue",name);
		    		$("#brandDialog").dialog("close");
		        }  
		    });  		 
	});