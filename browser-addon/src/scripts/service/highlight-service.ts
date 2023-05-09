export class HighlightService {
    prepareHighlight(startPath: string, endPath: string, startOffset: number, endOffset: number) {
        const startContainer = this.getNodeFromPath(startPath);
        if (startContainer == null) {
            return;
        }
        const endContainer = this.getNodeFromPath(endPath);
        if (endContainer == null) {
            return;
        }
        const range = document.createRange();
        range.setStart(startContainer, startOffset);
        range.setEnd(endContainer, endOffset);
        this.applyHighlight(startContainer, startOffset, endContainer, endOffset, range);
    }

    getNodeFromPath(path: string): Node | null {
        const pathArray = path.split('/');
        let currentNode: Element = document.body;

        for (let i = 0; i < pathArray.length - 1; i++) {
            const [tagName, index] = pathArray[i].replace(/[}]/g, '').split('{');
            const childNodes = currentNode.children;
            let matchingNode: Element | null = null;
            const filteredNodes = Array.from(childNodes)
                .filter(node => node.nodeName === tagName.toUpperCase());
            const idx = parseInt(index);
            if (idx < filteredNodes.length) {
                matchingNode = filteredNodes[idx];
            }

            if (!matchingNode) {
                console.error("No such node");
                return null;
            }

            currentNode = matchingNode;
        }

        const nodeIndex = parseInt(pathArray[pathArray.length - 1]);

        return currentNode.childNodes[nodeIndex];
    }

    applyHighlight(startContainer: Node, startOffset: number, endContainer: Node,
        endOffset: number, range: Range) {
        let root: Node | null = range.commonAncestorContainer;
        if (root && root.nodeType == Node.TEXT_NODE) {
            const markElement = document.createElement('span');
            markElement.classList.add("locus-highlight");
            range.surroundContents(markElement);
            return;
        }

        if (root && root.nodeType !== Node.ELEMENT_NODE) {
            root = root.parentElement;
        }

        if (!root) {
            return;
        }
        const whitespace = /^\s*$/;
        let nodes = Array.from(root.childNodes)
            .filter(node => range.comparePoint(node, 0) <= 0)
            .filter(node => range.comparePoint(node, node.nodeValue?.length ?? node.childNodes.length) >= 0)
        //    .filter(node => !whitespace.test(node.data));

        this.applySubhighlights(nodes, startContainer, startOffset, endContainer, endOffset, range);
    }

    applySubhighlights(nodes: Node[], startContainer: Node, startOffset: number, 
        endContainer: Node, endOffset: number, range: Range) {
        range.collapse();

        for (var i = 0; i < nodes.length; i++) {
            var rng = document.createRange();
            if (i == 0) {
                rng.setStart(startContainer, startOffset);
                rng.setEnd(nodes[i], nodes[i].nodeValue?.length ?? nodes[i].childNodes.length);
                if (startContainer.parentElement !== nodes[i] && startContainer !== nodes[i]) {
                    const newEndOffset = nodes[i].nodeValue?.length ?? nodes[i].childNodes.length;
                    this.applyHighlight(startContainer, startOffset, nodes[i], newEndOffset, rng);
                    continue;
                }
            } else if (i == nodes.length - 1) {
                rng.setStart(nodes[i], 0);
                rng.setEnd(endContainer, endOffset);
                if (endContainer.parentElement !== nodes[i] && endContainer !== nodes[i]) {
                    const newStartNode = nodes[i].nodeType == Node.TEXT_NODE ? nodes[i] : nodes[i].firstChild;
                    if(newStartNode) {
                        this.applyHighlight(newStartNode, 0, endContainer, endOffset, rng);
                    }
                    continue;
                }
            } else {
                rng.setStart(nodes[i], 0);
                rng.setEnd(nodes[i], nodes[i].nodeValue?.length ?? nodes[i].childNodes.length);
            }
            const markElement = document.createElement('span');
            markElement.classList.add("locus-highlight");
            rng.surroundContents(markElement);
            rng.collapse();
        }
    }
}