export interface Annotation {
    id: number;
    text: string;
    annotation: string;
    timestamp: number;
    type: string;
    owner: string;
    url: string;
    createdAt: Date;
    selectionStart: number;
    startElement: string;
    selectionEnd: number;
    endElement: string;
}
