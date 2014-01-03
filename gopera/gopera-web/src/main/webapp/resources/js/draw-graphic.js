function initialize(divName) {
	var sigInst = sigma.init(document.getElementById(divName))
			.drawingProperties({
				defaultLabelColor : '#9ba9d0',
				defaultLabelSize : 14,
				defaultLabelBGColor : '#644f4f',
				defaultLabelHoverColor : '#e1dd63',
				labelThreshold : 1,
				defaultEdgeType : 'curve'
			}).graphProperties({
				minNodeSize : 0.5,
				maxNodeSize : 5,
				minEdgeSize : 1,
				maxEdgeSize : 1
			}).mouseProperties({
				maxRatio : 4
			});
	return sigInst;
}

function bindEvents(sigInst){
	sigInst.bind('overnodes', function(event) {
		var nodes = event.content;
		var neighbors = {};
		sigInst.iterEdges(function(e) {
			if (nodes.indexOf(e.source) >= 0 || nodes.indexOf(e.target) >= 0) {
				neighbors[e.source] = 1;
				neighbors[e.target] = 1;
			}
		}).iterNodes(function(n) {
			if (!neighbors[n.id]) {
				n.hidden = 1;
			} else {
				n.hidden = 0;
			}
		}).draw(2, 2, 2);
	}).bind('outnodes', function() {
		sigInst.iterEdges(function(e) {
			e.hidden = 0;
		}).iterNodes(function(n) {
			n.hidden = 0;
		}).draw(2, 2, 2);
	});	
}

function drawGraph() {
	var files = [];
	files[0] = document.getElementById("comCaptacaoFile").value;
	files[1] = document.getElementById("semCaptacaoFile").value;
	
	var divGraphs = [];
	divGraphs[0] = 'sigma-with-edges';
	divGraphs[1] = 'sigma-without-edges';
	
	for (var i = 0; i < files.length; i++){
		var file = files[i];
		
		if (file != null && file != ''){
			var sigInst = initialize(divGraphs[i]);
			
			var gexf = new DOMParser().parseFromString(file, 'text/xml');
			
			sigInst.parseGexf(gexf);
			
			bindEvents(sigInst);
			
			sigInst.draw();
		}
	}
}