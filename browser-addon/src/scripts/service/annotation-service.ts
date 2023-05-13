import { Annotation } from "../dto/annotation";

export class AnnotationService {
    private apiUri = "http://localhost:8000/api/v1";

    constructor() {
    }

    async fetchAllAnnotations(url: string, token?: string): Promise<Annotation[]> {
        const params = new URLSearchParams({ url: url });
        const response = await fetch(`${this.apiUri}/annotations/all?` + params, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        });
        return await response.json();
    }

    async addAnnotation(url: string,
        pageAnnotation?: string,
        text?: string,
        startElement?: string,
        selectionStart?: number,
        endElement?: string,
        selectionEnd?: number,
        token?: string): Promise<Annotation> {

        const response = await fetch(`${this.apiUri}/annotations`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify({
                url: url,
                highlightedText: text,
                annotation: pageAnnotation,
                selectionStart: selectionStart,
                selectionEnd: selectionEnd,
                startElement: startElement,
                endElement: endElement
            })
        })
        return await response.json();
    }
}