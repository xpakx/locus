import { Annotation } from "../dto/annotation";

export class AnnotationService {
    private apiUri = "http://localhost:8000/api/v1";

    constructor() {
    }

    async fetchAllAnnotations(token?: string): Promise<Annotation[]> {
        const response = await fetch(`${this.apiUri}/annotations/all`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        });
        return await response.json();
    }
}