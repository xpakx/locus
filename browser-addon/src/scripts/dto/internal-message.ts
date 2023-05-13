export interface InternalMessage {
    action: string,
    url?: string;
    id?: number;
    annotation?: {
        pageAnnotation?: string,
        text?: string,
        startElement?: string,
        selectionStart?: number,
        endElement?: string,
        selectionEnd?: number
      }
}