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
}