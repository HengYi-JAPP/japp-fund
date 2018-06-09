export class CorporationSuggest {
  code: string;
  name: string;

  static assign(...sources: any[]): CorporationSuggest {
    const result = Object.assign(new CorporationSuggest(), ...sources);
    return result;
  }

  static toEntities(os: CorporationSuggest[], entities?: { [id: string]: CorporationSuggest }): { [id: string]: CorporationSuggest } {
    return (os || []).reduce((acc, cur) => {
      acc[cur.code] = CorporationSuggest.assign(cur);
      return acc;
    }, {...(entities || {})});
  }
}
