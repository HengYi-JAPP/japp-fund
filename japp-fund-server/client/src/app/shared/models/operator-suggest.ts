export class OperatorSuggest {
  name: string;
  hrId: string;
  oaId: string;

  static assign(...sources: any[]): OperatorSuggest {
    const result = Object.assign(new OperatorSuggest(), ...sources);
    return result;
  }

  static toEntities(os: OperatorSuggest[], entities?: { [id: string]: OperatorSuggest }): { [id: string]: OperatorSuggest } {
    return (os || []).reduce((acc, cur) => {
      cur = OperatorSuggest.assign(cur);
      acc[cur.key] = cur;
      return acc;
    }, {...(entities || {})});
  }

  get key(): string {
    const hrId = this.hrId || '';
    const oaId = this.oaId || '';
    return hrId + '-' + oaId;
  }
}
