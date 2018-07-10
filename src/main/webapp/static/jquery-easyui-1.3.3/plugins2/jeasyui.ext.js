/**
 * 整合一些基于Easyui的扩展实现. wuwz@live.com
 */

$.easyuiExt = {
	// 获取主界面的Tab控件
	getMainTab: function() {
		var target = '#easyui-main-tabs';
		var tabs = jQuery(target);
		if(!tabs) {
			tabs = top.jQuery(target);
		}
		return tabs;
	},
	// 打开一个新的tab页
	openTab: function(options) {
		var defaults = {
			id: 'tabs-test',
			inIFrame: true, //是否使用iframe加载完整网页？
			title: '新标签页',
			href: 'javascript:;',
			iconCls: 'icon-file',
			closable: true
		};
		options = $.extend(defaults, options);

		var $tabs = $.easyuiExt.getMainTab();

		if($tabs.tabs('exists', options.title)) {
			$tabs.tabs('select', options.title);
		} else {
			var tabOptions = {
				id: options.id,
				title: options.title,
				closable: options.closable,
				iconCls: options.iconCls,
				href: options.href
			}
			if(options.icon) {
				tabOptions.iconCls = options.iconCls;
			}
			if(options.inIFrame) {
				delete tabOptions.href;
				tabOptions.content = '<iframe scrolling="hidden" frameborder="0"  src="' + options.href + '" style="width:100%;height:99.5%;"></iframe>';
			}
			$tabs.tabs('add', tabOptions);
		}
		return $tabs;
	},
	// 将form表单元素的值序列化成对象
	getJson: function(form) {
		var o = {};
		$.each(form.serializeArray(), function(index) {
			var v = this['value'] != null && this['value'] != '' ? this['value'] : null;

			if(o[this['name']]) {
				o[this['name']] = o[this['name']] + "," + v;
			} else {
				o[this['name']] = v;
			}
		});
		return o;
	},

	// 绑定指定的easyui-tabs右键菜单，包含一系列的快捷关闭操作
	bindTabsContextMenu: function(_tabs) {
		var exclude_title = '欢迎使用'; //不允许关闭的索引

		/*双击关闭TAB选项卡*/
		$('.tabs-inner').live('dblclick', function() {
			var t = $(this).children("span").text();
			if(t != exclude_title) {
				_tabs.tabs('close', t);
			}
		});

		var mmHtml = '<div id="main-tabs-mm" class="easyui-menu" style="width:175px;display:none;">' +
			/*'<div id="mm-tabreload" data-options="name:6">刷新</div>' +*/
			'<div id="mm-tabclose" iconCls="icon-no" data-options="name:1">关闭</div>' +
			'<div id="mm-tabcloseother" data-options="name:3">关闭其他</div>' +
			' <div id="mm-tabcloseall" iconCls="icon-cancel" data-options="name:2">全部关闭</div>' +
			'<div class="menu-sep"></div>' +
			'<div id="mm-tabcloseright" iconCls="icon-undo" data-options="name:4">当前页左侧全部关闭</div>' +
			'<div id="mm-tabcloseleft" iconCls="icon-redo" data-options="name:5">当前页右侧全部关闭</div> </div>';

		$('body').append($(mmHtml));

		_tabs.tabs({
			onContextMenu: function(e, title, index) {
				e.preventDefault();
				if(index > 0 && title != exclude_title) {
					$('#main-tabs-mm').menu('show', {
						left: e.pageX,
						top: e.pageY
					}).data("tabTitle", title);
				}
			}
		});
		//右键菜单click
		$("#main-tabs-mm").menu({
			onClick: function(item) {
				if(item.name == 1) {
					//关闭当前
					var t = $('#main-tabs-mm').data("tabTitle");
					if(t != exclude_title) {
						_tabs.tabs('close', t);
					}
				} else if(item.name == 2) {
					//关闭所有
					$('.tabs-inner span').each(function(i, n) {
						var t = $(n).text();
						if(t != exclude_title) {
							_tabs.tabs('close', t);
						}
					});
				} else if(item.name == 3) {
					// 除此之外全部关闭
					var currtab_title = $('#main-tabs-mm').data("tabTitle");
					$('.tabs-inner span').each(function(i, n) {
						var t = $(n).text();
						if(t != currtab_title && t != exclude_title) {
							_tabs.tabs('close', t);
						}
					});
					
					_tabs.tabs('select',currtab_title);
				} else if(item.name == 4) {
					// 左侧
					var prevall = $('.tabs-selected').prevAll();
					if(prevall.length == 0) {
						return false;
					}
					prevall.each(function(i, n) {
						var t = $('a:eq(0) span', $(n)).text();
						if(t != exclude_title) {
							_tabs.tabs('close', t);
						}
					});

				} else if(item.name == 5) {
					// 右侧
					var nextall = $('.tabs-selected').nextAll();
					if(nextall.length == 0) {
						return false;
					}
					nextall.each(function(i, n) {
						var t = $('a:eq(0) span', $(n)).text();
						if(t != exclude_title) {
							_tabs.tabs('close', t);
						}
					});
				} else {
					// 刷新
					var current_tab = _tabs.tabs('getSelected');
					var html = $(current_tab).html();
					var options = {
						content: html
					};
					if(html.indexOf("iframe") == -1) {
						//delete options.content;
						//options.href = 'home.html'; //TODO
					}
					_tabs.tabs('update', {
						tab: current_tab,
						options: options
					});
				}
			}
		});

	}
};


// 使panel和datagrid在加载时提示
$.fn.panel.defaults.loadingMessage = '加载中...';
$.fn.datagrid.defaults.loadMsg = '加载中...';

// panel关闭时回收内存，主要用于layout使用iframe嵌入网页时的内存泄漏问题
$.fn.panel.defaults.onBeforeDestroy = function() {
	var frame = $('iframe', this);
	try {
		if(frame.length > 0) {
			for(var i = 0; i < frame.length; i++) {
				frame[i].src = '';
				frame[i].contentWindow.document.write('');
				frame[i].contentWindow.close();
			}
			frame.remove();
			if(navigator.userAgent.indexOf("MSIE") > 0) { // IE特有回收内存方法
				try {
					CollectGarbage();
				} catch(e) {}
			}
		}
	} catch(e) {}
};

/*Tree系列的平滑数据格式支持 start*/
// 扩展tree，使其支持平滑数据格式
$.fn.tree.defaults.loadFilter = function(data, parent) {
	var opt = $(this).data().tree.options;
	var idFiled, textFiled, parentField;
	if(opt.parentField) {
		idFiled = opt.idFiled || 'id';
		textFiled = opt.textFiled || 'text';
		parentField = opt.parentField;
		var i, l, treeData = [],
			tmpMap = [];
		for(i = 0, l = data.length; i < l; i++) {
			tmpMap[data[i][idFiled]] = data[i];
		}
		for(i = 0, l = data.length; i < l; i++) {
			if(tmpMap[data[i][parentField]] &&
				data[i][idFiled] != data[i][parentField]) {
				if(!tmpMap[data[i][parentField]]['children'])
					tmpMap[data[i][parentField]]['children'] = [];
				data[i]['text'] = data[i][textFiled];
				tmpMap[data[i][parentField]]['children'].push(data[i]);
			} else {
				data[i]['text'] = data[i][textFiled];
				treeData.push(data[i]);
			}
		}
		return treeData;
	}
	return data;
};

// 扩展combotree，使其支持平滑数据格式
$.fn.combotree.defaults.loadFilter = $.fn.tree.defaults.loadFilter;

// 扩展treegrid，使其支持平滑数据格式
$.fn.treegrid.defaults.loadFilter = function(data, parentId) {
	var opt = $(this).data().treegrid.options;
	var idFiled, textFiled, parentField;
	if(opt.parentField) {
		idFiled = opt.idFiled || 'id';
		textFiled = opt.textFiled || 'text';
		parentField = opt.parentField;
		var i, l, treeData = [],
			tmpMap = [];
		for(i = 0, l = data.length; i < l; i++) {
			tmpMap[data[i][idFiled]] = data[i];
		}
		for(i = 0, l = data.length; i < l; i++) {
			if(tmpMap[data[i][parentField]] &&
				data[i][idFiled] != data[i][parentField]) {
				if(!tmpMap[data[i][parentField]]['children'])
					tmpMap[data[i][parentField]]['children'] = [];
				data[i]['text'] = data[i][textFiled];
				tmpMap[data[i][parentField]]['children'].push(data[i]);
			} else {
				data[i]['text'] = data[i][textFiled];
				treeData.push(data[i]);
			}
		}
		return treeData;
	}
	return data;
};
/*Tree系列的平滑数据格式支持 end*/

// 为datagrid、treegrid增加表头菜单，用于显示或隐藏列.
var createGridHeaderContextMenu = function(e, field) {
	e.preventDefault();
	var grid = $(this); /* grid本身 */
	var headerContextMenu = this.headerContextMenu; /* grid上的列头菜单对象 */
	if(!headerContextMenu) {
		var tmenu = $('<div style="width:100px;"></div>').appendTo('body');
		var fields = grid.datagrid('getColumnFields');
		for(var i = 0; i < fields.length; i++) {
			var fildOption = grid.datagrid('getColumnOption', fields[i]);
			if(!fildOption.hidden) {
				$('<div iconCls="tick" field="' + fields[i] + '"/>').html(
					fildOption.title).appendTo(tmenu);
			} else {
				$('<div iconCls="bullet_blue" field="' + fields[i] + '"/>')
					.html(fildOption.title).appendTo(tmenu);
			}
		}
		headerContextMenu = this.headerContextMenu = tmenu.menu({
			onClick: function(item) {
				var field = $(item.target).attr('field');
				if(item.iconCls == 'tick') {
					grid.datagrid('hideColumn', field);
					$(this).menu('setIcon', {
						target: item.target,
						iconCls: 'bullet_blue'
					});
				} else {
					grid.datagrid('showColumn', field);
					$(this).menu('setIcon', {
						target: item.target,
						iconCls: 'tick'
					});
				}
			}
		});
	}
	headerContextMenu.menu('show', {
		left: e.pageX,
		top: e.pageY
	});
};
$.fn.datagrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;
$.fn.treegrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;

// 通用错误提示
// 用于datagrid/treegrid/tree/combogrid/combobox/form加载数据出错时的操作
var easyuiErrorFunction = function(XMLHttpRequest) {
	$.messager.progress('close');

	var status = XMLHttpRequest.status;
	var statusText = XMLHttpRequest.statusText;

	var errorMsg = "系统出现了一个未知的未指明的错误,如果多次出现该问题,请尝试联系管理员。";
	if(status && statusText != null && statusText != '') {
		errorMsg = "Status:" + status + ",StatusText:" + statusText;
		errorMsg += ",ResponseText:" + XMLHttpRequest.responseText;
	}
	console.error(errorMsg);
	parent.$.messager.alert('提示', '抱歉,系统出问题了...<br/>请打开开发者工具(F12)查看详情！', 'error');
};
$.fn.datagrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.treegrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.tree.defaults.onLoadError = easyuiErrorFunction;
$.fn.combogrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.combobox.defaults.onLoadError = easyuiErrorFunction;
$.fn.form.defaults.onLoadError = easyuiErrorFunction;
