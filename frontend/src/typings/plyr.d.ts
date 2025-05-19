declare module 'plyr' {
  export interface Source {
    type?: string;
    sources?: { src: string; type?: string; size?: number | string }[];
    title?: string;
  }

  export interface PlyrQualityOptions {
    default?: number;
    options?: number[];
    forced?: boolean;
    onChange?: (newQuality: number) => void;
  }

  export interface PlyrOptions {
    controls?: string[];
    autoplay?: boolean;
    settings?: string[];
    quality?: number | PlyrQualityOptions;
  }

  export default class Plyr {
    constructor(element: Element | string, options?: PlyrOptions);

    source: Source;

    play(): Promise<void>;
    pause(): void;
    destroy(): void;

    on(event: string, callback: () => void): void;
  }
}
