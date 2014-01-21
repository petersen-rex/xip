$(function(){
	var oTable=null;
	var rowSelected=null;
	var gridId='#service-grid';

	// create toaster for later use
	var toaster = $.toaster({showTime: 2000, centerX:true, centerY:true});
	
	// Setup Splitter / layout
	$("#splitterContainer").splitter({minAsize:380,maxAsize:500, initSize:20, splitVertical:true,A:$('#leftPane'),B:$('#rightPane'),slave:$("#rightSplitterContainer"),closeableto:0});
	$("#rightSplitterContainer").splitter({minBsize:100, maxBsize:400, initSize:70, splitHorizontal:true,A:$('#rightTopPane'),B:$('#rightBottomPane'),closeableto:0});


    // Setup Menu Actions
	// ** Add **
	$("#addServiceText").button();
    $(".addService").click(function() {
        var menu = $( "#addServiceDropdown").show().position({
            my: "left top",
            at: "left bottom",
            of: this
        });
        $( document ).one( "click", function() {
            menu.hide();
        });
        return false;
    })
    $("#addServiceGroup").buttonset();
    $("#addServiceDropdown").hide().menu();
    
    // ** Update **
    $("#updateService").button().click(updateService);

    function updateService(){
		var localData = {
			serviceAction: $("#serviceAction").val(),
			serviceInputMappings: $("#serviceInputMappings").val(),
			serviceName: $("#serviceName").val(),
			serviceOutputMappings: $("#serviceOutputMappings").val(),
			serviceProvider: $("#serviceProvider").val(),
			serviceRoles: $("#serviceRoles").val(),
			serviceText: $("#serviceText").val(),
			serviceType: $("#serviceType").val()
		};
    	jQuery.ajax({
			type : "POST",
			url : "/delegate/appServices?cmd=setServiceDefinition",
			dataType: "json",
			cache : false,
			data: localData,
			success : function(data) {
				toaster.toast(data.message);
				/*
				var currentRow = fnGetSelectedData(oTable);
				$.each(localData, function(index, value){
					currentRow[index] = localData[index];
				});
				*/
				refreshData(localData.serviceName);
			}
    	});
    }
    
    // Setup Control-S save behavior
    $(window).keydown(function(event) {
	    if (!(event.which == 83 && event.metaKey)) return true;
	    updateService();
	    event.preventDefault();
	    return false;
	});


    // ** Delete **
    $("#deleteService").button().click(function(){
    	var currentRow = fnGetSelectedData(oTable); 
    	if(window.confirm('Delete service definition: ' + currentRow.serviceName + '?')){
	    	jQuery.ajax({
				type : "POST",
				url : "/delegate/appServices?cmd=deleteServiceDefinition",
				dataType: "json",
				cache : false,
				data: {serviceName: currentRow.serviceName},
				success : function(data) {
					toaster.toast(data.message);
					refreshData();
				}
			});
    	}
    });
    
    
	// Setup Service Grid
	function doOnRowClick( e ) {
        if ( !$(this).hasClass('row_selected') ) {
            $(rowSelected).removeClass('row_selected');
            $(this).addClass('row_selected');
            var position=this.offsetTop;
    	    var el = $(gridId+'_wrapper .dataTables_scrollBody')[0];
            if(position<el.scrollTop){
            	el.scrollTop=position
            } else if (position+this.clientHeight>el.scrollTop+455){
            	el.scrollTop=position-455+this.clientHeight;
            }
        }
        rowSelected=this;
		var data = oTable.fnGetData( rowSelected );
		doOnDataChanged(data);
	}

	// Update Form Fields when data Changed
	function doOnDataChanged(data){
    	$("#serviceUrl").val("/delegate/appServices?cmd=executeService&service=" + data.serviceName);
		$.each(data,function(index,value){
			$('#'+index).val(value);
		});
		
		$.Topic('ServiceDefinitions-RowChanged').publish(data);
		
		/*
		 	$.Topic('ChangedRecord').subscribe( function(data){
				$.each(data,function(index,value){
					$('#'+index).val(value);
				});
 			});
		 */
    }
    
	oTable = $('#service-grid').dataTable( {
		aoColumns  : 	[
						{mDataProp:"serviceName", sWidth:"75%"},
						{mDataProp:"serviceType", sWidth:"25%"}],
		aaSorting: [[ 0, "desc" ]],
		bInfo:true,
		bProcessing: true,
		bAutoWidth:false,
		sAjaxDataProp: "data",
		bDeferRender: true,
	    sScrollY: "455px",
	    sScrollX: "100%",
	    sScrollXInner: "100%",
	    bPaginate:false,
	    sDom: "frtiS",
		fnCreatedRow: function(tr, data, i){
			$(tr).click(doOnRowClick);
		},
		oLanguage: {
			"sSearch": "Quick Search:"
		}
	} );

	function refreshData(serviceName){
		oTable.fnReloadAjax("/delegate/appServices?cmd=getServiceDefinitions&r=0&c=200", function(data){
			if(serviceName!=undefined){
				$.each(oTable.$('tr'), function(){
					if(oTable.fnGetData(this).serviceName==serviceName){
						doOnRowClick.call($(this));
					}
				});
			} else {
				doOnRowClick.call($(gridId + ' tr')[1]);
			}
		});
	}

	refreshData();
	
	function fnGetSelectedData( oTableLocal )
	{
	    var rowSelected = oTableLocal.$('tr.row_selected').first();
		return oTableLocal.fnGetData(rowSelected[0]);
	}

	$("#leftPane").resize(function(){
		oTable.fnAdjustColumnSizing();
		$('.dataTables_scrollBody').css("width", "100%");
		$('.dataTables_scrollHead').css("width", "100%");
		$('.dataTables_scrollHeadInner').css("width", "100%");
		$(gridId).css("width", "100%");
		$('.dataTables_scrollHeadInner table').css("width", "100%");
		
	});
	var elWrapper = $(gridId+'_wrapper');
	elWrapper[0].tabIndex=0;
	
	// ** Setup Arrow Key Handling for Grid
	elWrapper.keydown(function(e){
	    var bReturn = true;
	    var newRow = null;
	    switch(e.keyCode){
		    case 38 : {
		    	bReturn = false;
		    	newRow = $(rowSelected).prev()[0];
		    	if(typeof newRow!='undefined' && newRow!=rowSelected){
		    		doOnRowClick.call(newRow);
		    	}
		    	break;
		    }
		    case 40 : {
		    	bReturn = false;
		    	newRow = $(rowSelected).next()[0];
		    	if(typeof newRow!='undefined' && newRow!=rowSelected){
		    		doOnRowClick.call(newRow);
		    	}
		    	break;
		    }
	    }
		return bReturn;
	});

	
	$("#executeService").click(function(){
		var theUrl = $("#serviceUrl").val() + $("#serviceExtraParms").val();
		jQuery.ajax({
			type : "GET",
			url : theUrl,
			dataType: "text",
			cache : false,
			success : function(data) {
				$("#serviceResults").html(data.replace(/\n/g, "<br>"));
			}
		});
	});

});
