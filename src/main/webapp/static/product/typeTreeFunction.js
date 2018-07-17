//这个复用性不强
//如dlg3

var treeType;
var p_id;
var t_id;
function chooseGoodsType(s_treeType, s_p_id, s_t_id) {
	treeType = s_treeType;
	p_id = s_p_id;
	t_id = s_t_id;
	
	//window.alert( '  sss  '+treeType);
	if (treeType == 0) { // 添加商品的那个类别
		$("#dlg3").dialog("open").dialog("setTitle", "选择商品类别");
		$("#typeTree").tree({
			url : '/admin/goodsType/loadTreeInfo?parentId=' + p_id,
			onLoadSuccess : function(node, data) {
				var rootNode = $("#typeTree").tree('getRoot');
				$("#typeTree").tree('expand', rootNode.target);
			}
		});
	} else { // 搜索商品的那个类别
		/*var top = $("#typeId2").offset().top;
		var left = $("#typeId2").offset().left;
		$('#dlg3').window('open').window('resize', {
			width : '180px',
			height : '300px',
			top : top,
			left : left
		});*/
		$("#dlg3").dialog("open").dialog("setTitle", "选择商品类别");
		
		$("#typeTree").tree({
			url : '/admin/goodsType/loadTreeInfo?parentId=' + p_id,
			onLoadSuccess : function(node, data) {
				var rootNode = $("#typeTree").tree('getRoot');
				$("#typeTree").tree('expand', rootNode.target);
			}
		});
	}
}

function saveGoodsTypeChoose() {
	if (treeType == 0) {
		var node = $('#typeTree').tree('getSelected'); // 获取选中节点
		if (node != null && node.id != t_id) { // 这边改成t_id，因为根节点无法被选中
			$("#typeId").val(node.id);

			$("#typeName").val(node.text);
		}
		$("#dlg3").dialog("close");
	}else{
		var node = $('#typeTree').tree('getSelected'); // 获取选中节点
		if (node != null && node.id != t_id) { // 这边改成t_id，因为根节点无法被选中
			$("#typeId2").val(node.id);
			$("#typeName2").val(node.text);
			searchGoods();
		}
		$("#dlg3").dialog("close");
	}
}

function closeGoodsTypeChooseDialog(){
	$("#dlg3").dialog("close");
}
